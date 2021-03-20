package com.ttds.cw3.Transaction;

import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Factory.AnalysisFactory;
import com.ttds.cw3.Factory.ReaderFactory;
import com.ttds.cw3.Interface.*;
import com.ttds.cw3.Strategy.StrategyType;
import com.ttds.cw3.Tools.DocAnalysis;
import com.ttds.cw3.Tools.DocReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@Component
public final class ModelManager implements ModelManagerInterface
{
    private String filePath = "assets/";
    private String stopWordFile = "englishST.txt";
    private StrategyType stopWordReadType = StrategyType.txt;

    private PreProcessing preProcessing;
    private DocsRepository docsRepository;
    private DocReader reader;
    private DocAnalysis converter;
    private Properties props;

    private final AtomicInteger idGenerator;

    private int thread = 5;

    @Autowired
    public ModelManager(DocsRepository docsRepository)
    {
        this.idGenerator = new AtomicInteger();
        this.docsRepository = docsRepository;

        loadProperties("application.properties");

        initDataAndDoc();

        System.out.printf("Model Manager构造完成。\n");
    }

    public boolean setFilePath()
    {
        if(props==null)
        {
            return false;
        }

        filePath = props.getProperty("filePath").trim();

        preProcessing.setFilePath(filePath);
        reader.setFilePath(filePath);

        return true;
    }

    public boolean setStopWordFile()
    {
        if(props==null)
        {
            return false;
        }

        String[] st = props.getProperty("stopWordFile").trim().split("#");
        stopWordFile = st[0].trim();
        stopWordReadType = StrategyType.valueOf(st[1].trim());

        preProcessing.setStopWordFile(stopWordFile, stopWordReadType);

        return true;
    }

    public void initDataAndDoc()
    {
        boolean reloadFiles = false;

        if(props!=null)
        {
            reloadFiles = Boolean.parseBoolean(props.getProperty("reloadFiles").trim());
        }

        if(reloadFiles || docsRepository.countDoc()<=0)
        {
            if(props!=null)
            {
                docsRepository.clear();
                String token = " ";
                String[] st = props.getProperty("dataFiles").trim().split(",");
                for(int i=0;i<st.length;i++)
                {
                    String[] st2 = st[i].trim().split("#");
                    String file = st2[0].trim();
                    StrategyType type;
                    try
                    {
                        type = StrategyType.valueOf(st2[1].trim());
                    }
                    catch (Exception e)
                    {
                        System.out.printf("所写的读取类型不存在！！\n",file);
                        e.printStackTrace();
                        continue;
                    }
                    if(st2.length>2)
                        token = st2[2].trim();

                    doProcess(file,type, token);
                }
            }
        }
    }

    public boolean loadProperties(String name)
    {
        boolean success;
        boolean removeStopWords = true;
        boolean stem = true;
        String pattern = "[^\\w]";
        try
        {
            props = PropertiesLoaderUtils.loadAllProperties(name);
            filePath = props.getProperty("filePath").trim();
            thread = Integer.parseInt(props.getProperty("thread").trim());

            String[] st = props.getProperty("stopWordFile").trim().split("#");
            stopWordFile = st[0].trim();
            stopWordReadType = StrategyType.valueOf(st[1].trim());

            pattern = props.getProperty("pattern").trim();
            removeStopWords = Boolean.parseBoolean(props.getProperty("removeStopWords").trim());
            stem = Boolean.parseBoolean(props.getProperty("stem").trim());

            success = true;
        }
        catch (IOException e)
        {
            success = false;
            props = null;
            System.out.printf("Model Manager读取application.properties文件出现问题！\n");
            e.printStackTrace();
        }

        preProcessing = new PreProcessing(filePath);
        preProcessing.setStopWordFile(stopWordFile, stopWordReadType);
        preProcessing.setBremoveStopWords(removeStopWords);
        preProcessing.setBstem(stem);
        preProcessing.setPattern(pattern);

        reader = new DocReader(filePath);
        converter = new DocAnalysis();

        return success;
    }

    private void doProcess(int num, int start, int end, List<Doc> list)
    {
        for(int i=start;i<end;i++)
        {
            // 互斥锁
            Doc doc = list.get(i);
            byte[] lock = new byte[0];
            synchronized(lock)
            {
                int id = idGenerator.incrementAndGet();
                while (docsRepository.containDoc(Integer.toString(id))) {
                    id = idGenerator.incrementAndGet();
                }
                doc.setId(Integer.toString(id));
                docsRepository.addDoc(doc);
            }

            HashMap<String,ArrayList<Integer>> tokens = new HashMap<>();
            synchronized(preProcessing)
            {
                preProcessing.doProcessing(doc.getText());
                if(thread<=0)
                    tokens = preProcessing.getTerms();
                else
                {
                    HashMap<String,ArrayList<Integer>> map = preProcessing.getTerms();

                    Iterator iter = map.entrySet().iterator();
                    while (iter.hasNext())
                    {
                        HashMap.Entry entry = (HashMap.Entry) iter.next();
                        String key = (String)entry.getKey();
                        ArrayList<Integer> value = deepCopy((ArrayList<Integer>)entry.getValue());
                        tokens.put(key,value);
                    }
                }
            }

            Iterator iter = tokens.entrySet().iterator();
            while (iter.hasNext())
            {
                HashMap.Entry entry = (HashMap.Entry) iter.next();
                String term = (String)entry.getKey();
                ArrayList<Integer> pos = (ArrayList<Integer>)entry.getValue();

                // 互斥锁
                byte[] lock2 = new byte[0];
                synchronized(lock2)
                {
                    for(int j=0;j<pos.size();j++)
                    {

                        docsRepository.addTermVector(term,doc.getId(),pos.get(j));
                        docsRepository.addDocVector(doc.getId(),doc.getName(), term);
                    }
                }
            }

            System.out.printf("\b\b\b\b\b"+String.format("%.2f",100.0*docsRepository.getDocSize()/num));
        }
    }

    private void doProcess(String fileName, StrategyType type, String token)
    {
        ArrayList data = (ArrayList)reader.get(fileName, ReaderFactory.get(type));
        final int max = 10000;
        int size = data.size();
        int all = (int)Math.ceil(1.0*size/max);
        for(int k=0;k<all;k++)
        {
            //long startTime = System.currentTimeMillis();

            int start = k*max;
            int end = (k+1)*max;
            if(end>size)
                end = size;
            List sub = data.subList(start,end);
            List<Doc> list = converter.process(AnalysisFactory.get(type),sub,token);
            int num = list.size();
            if(list==null)
                return;

            if(thread<=0)
                doProcess(size, 0, num, list);
            else
            {
                ExecutorService pool = Executors.newCachedThreadPool();
                int per = num/thread;
                for (int s = 0, e = per; e <= num;)
                {
                    int finalS = s, finalE = e;
                    pool.execute(new Runnable() {
                        @Override
                        public void run()
                        {
                            final int start = finalS,end = finalE;
                            doProcess(size, start, end, list);
                        }
                    });

                    if(e==num)
                        break;

                    s = e;
                    e += per;
                    if(e>num)
                        e = num;
                }
                pool.shutdown();
                try
                {
                    pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //long endTime = System.currentTimeMillis();
            //System.out.println("时间："+(endTime-startTime));
        }
    }

    public PreProcessingInterface getPreProcessing()
    {
        return preProcessing;
    }

    public DocInterface getDoc(String id)
    {
        return docsRepository.getDoc(id);
    }

    public DocVectorInterface getDvByDocid(String docid){return docsRepository.getDvByDocid(docid);}

    public TermVectorInterface getTermByTerm(String term){return docsRepository.getTermByTerm(term);}

    public long getDocSize(){return docsRepository.getDocSize();}

    public long getTermsSize(){return docsRepository.getTermsSize();}

    public List<TermVectorInterface> getTerms()
    {
        List y = docsRepository.getTerms();
        return (List<TermVectorInterface>)y;
    }

    public List<DocVectorInterface> getDvs()
    {
        List y = docsRepository.getDvs();
        return (List<DocVectorInterface>)y;
    }

    public List<DocInterface> getDocs()
    {
        List y = docsRepository.getDocs();
        return (List<DocInterface>)y;
    }

    public static <T> ArrayList<T> deepCopy(ArrayList<T> srcList) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(srcList);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream inStream = new ObjectInputStream(byteIn);
            ArrayList<T> destList = (ArrayList<T>) inStream.readObject();
            return destList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
