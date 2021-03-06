package com.ttds.cw3.Transaction;

import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Data.DocVector;
import com.ttds.cw3.Data.TermVector;
import com.ttds.cw3.Factory.AnalysisFactory;
import com.ttds.cw3.Factory.ReaderFactory;
import com.ttds.cw3.Interface.*;
import com.ttds.cw3.Strategy.StrategyType;
import com.ttds.cw3.Tools.DocAnalysis;
import com.ttds.cw3.Tools.DocReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.*;

import java.util.concurrent.ConcurrentHashMap;
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

    private DocsRepository docsRepository;
    private DocReader reader;
    private DocAnalysis converter;
    private Properties props;

    private List<String> stopWords;
    private boolean BremoveStopWords = true;
    private boolean Bstem = true;
    private String pattern = "[\\w]+";

    private final AtomicInteger idGenerator;

    private int thread = 5;

    @Autowired
    public ModelManager(DocsRepository docsRepository)
    {
        this.idGenerator = new AtomicInteger((int)docsRepository.getDocDBSize()+1);
        this.docsRepository = docsRepository;

        loadProperties("application.properties");

        initDataAndDoc();

        System.out.printf("Model Manager构造完成。\n");
    }

    public void test()
    {
        int max = 5000;
        int size = (int)docsRepository.getTermsDBSize();
        int all = (int)Math.ceil(1.0*size/max);
        ArrayList<String> list = new ArrayList<>();

        for(int w=0;w<5;w++)
        {
            for(int k=0;k<all;k++)
            {
                List<TermVector> data = docsRepository.termcDBfind(k,max);
                for(int i=0;i<data.size();i++)
                {
                    String key = data.get(i).getTerm();
                    long startTime = System.currentTimeMillis();
                    TermVectorInterface t = getTermByTerm("love");
                    long endTime = System.currentTimeMillis();
                    System.out.println(endTime-startTime);
                    if(endTime-startTime>20 && !list.contains(t.getTerm()))
                    {

                    }
                    //System.out.print("\b\b\b\b\b\b\b"+String.format("%.2f",100.0*(k*max+i)/size)+"%");
                }
            }
            System.out.println();
            System.out.println(list.size());
        }
    }

    public boolean setFilePath()
    {
        if(props==null)
        {
            return false;
        }

        filePath = props.getProperty("filePath").trim();

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
        String stopWordFile = st[0].trim();
        StrategyType stopWordReadType = StrategyType.valueOf(st[1].trim());
        stopWords = converter.txtsfrom(AnalysisFactory.get(stopWordReadType),reader.get(stopWordFile, ReaderFactory.get(stopWordReadType)));

        return true;
    }

    public void initDataAndDoc()
    {
        boolean reloadFiles = false;

        if(props!=null)
        {
            reloadFiles = Boolean.parseBoolean(props.getProperty("reloadFiles").trim());
        }

        if(reloadFiles || docsRepository.getDocDBSize()<=0)
        {
            docsRepository.clear();
            if(props!=null)
            {
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

                    System.out.println("文件"+file+"数据读取:");
                    doProcess(file,type, token);
                    System.out.println();
                }
            }
        }
    }

    public void init()
    {
        initDB(thread,0);
        initDB(thread,2);
        initDB(thread,1);
    }

    private void initDB(int thread,int i)
    {
        final int max = 5000;
        int size = 0;
        if(i==2)
        {
            System.out.println("terms数据缓存:");
            size = (int)docsRepository.getTermsDBSize();
        }
        else if(i==1)
        {
            System.out.println("dvs数据缓存:");
            size = (int)docsRepository.getDvsDBSize();
        }
        else
        {
            System.out.println("docs数据缓存:");
            size = (int)docsRepository.getDocDBSize();
        }

        int all = (int)Math.ceil(1.0*size/max);
        long startTime = System.currentTimeMillis();
        for(int k=0;k<all;k++)
        {
            List data;
            if(i==2)
            {
                data = docsRepository.termcDBfind(k,max);
            }
            else if(i==1)
            {
                data = docsRepository.dvsDBfind(k,max);
            }
            else
            {
                data = docsRepository.docDBfind(k,max);
            }
            int num = max;
            if (num > data.size())
                num = data.size();

            if (data == null)
                continue;

            if(thread<=0)
                inThread(size,0, num,i,data);
            else
            {
                ExecutorService pool = Executors.newCachedThreadPool();
                int per = num/thread;
                if(per < 1)
                    per = 1;
                for (int s = 0, e = per; e <= num;)
                {
                    int finalS = s, finalE = e;
                    int finalNum = num;
                    int finalI = i;
                    pool.execute(new Runnable() {
                        @Override
                        public void run()
                        {
                            final int start = finalS,end = finalE;
                            final int type = finalI,size = finalNum;
                            inThread(size,start, end,type,data);
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
        }

        long endTime = System.currentTimeMillis();
        System.out.println();
        System.out.println("用时："+(endTime-startTime));
    }

    private void inThread(int size, int start,int end,int type,List list)
    {
        for(int i=start;i<end;i++)
        {
            if(type==2)
            {
                TermVector tv = (TermVector)list.get(i);
                TermVector re = docsRepository.getTermByTerm(tv.getTerm());
                System.out.print("\b\b\b\b\b\b\b"+String.format("%.2f",100.0*i/size)+"%");
            }
            else if(type==1)
            {
                DocVector dv = (DocVector)list.get(i);
                DocVector re = docsRepository.getDvByDocid(dv.getDocid());
                System.out.print("\b\b\b\b\b\b\b"+String.format("%.2f",100.0*i/size)+"%");
            }
            else
            {
                Doc d = (Doc) list.get(i);
                Doc re = docsRepository.getDoc(d.getId());
                System.out.print("\b\b\b\b\b\b\b"+String.format("%.2f",100.0*i/size)+"%");
            }
        }
    }

    public boolean loadProperties(String name)
    {
        boolean success;
        String[] st = {"englishST.txt","txt"};
        int max = 100000;
        try
        {
            props = PropertiesLoaderUtils.loadAllProperties(name);
            filePath = props.getProperty("filePath").trim();
            thread = Integer.parseInt(props.getProperty("thread").trim());
            max = Integer.parseInt(props.getProperty("max").trim());
            int smax = Integer.parseInt(props.getProperty("smax").trim());
            int rmax = Integer.parseInt(props.getProperty("rmax").trim());
            docsRepository.setMax(Math.max(smax,rmax));

            st = props.getProperty("stopWordFile").trim().split("#");

            pattern = props.getProperty("pattern").trim();
            BremoveStopWords = Boolean.parseBoolean(props.getProperty("removeStopWords").trim());
            Bstem = Boolean.parseBoolean(props.getProperty("stem").trim());

            success = true;
        }
        catch (IOException e)
        {
            success = false;
            props = null;
            System.out.printf("Model Manager读取application.properties文件出现问题！\n");
            e.printStackTrace();
        }

        reader = new DocReader(filePath,max);
        converter = new DocAnalysis();

        String stopWordFile = st[0].trim();
        StrategyType stopWordReadType = StrategyType.valueOf(st[1].trim());
        stopWords = converter.txtsfrom(AnalysisFactory.get(stopWordReadType),reader.get(stopWordFile, ReaderFactory.get(stopWordReadType)));

        return success;
    }

    private void doProcess(int finish, int num, int start, int end, List<Doc> list)
    {
        PreProcessing preProcessing = new PreProcessing(stopWords,BremoveStopWords,Bstem,pattern);
        for(int i=start;i<end;i++)
        {
            // 互斥锁
            Doc doc = list.get(i);
            byte[] lock = new byte[0];
            synchronized(lock)
            {
                int id = idGenerator.incrementAndGet();
                while (docsRepository.containDoc(Integer.toString(id)))
                {
                    id = idGenerator.incrementAndGet();
                }
                doc.setId(Integer.toString(id));
                docsRepository.addDoc(id,doc);
            }

            preProcessing.doProcessing(doc.getText());
            ConcurrentHashMap<String,ArrayList<Integer>> tokens = preProcessing.getTerms();

            Iterator iter = tokens.entrySet().iterator();
            while (iter.hasNext())
            {
                ConcurrentHashMap.Entry entry = (ConcurrentHashMap.Entry) iter.next();
                String term = (String)entry.getKey();
                ArrayList<Integer> pos = (ArrayList<Integer>)entry.getValue();

                // 互斥锁
                for(int j=0;j<pos.size();j++)
                {
                    byte[] lock2 = new byte[0];
                    synchronized(lock2)
                    {
                        docsRepository.addTermVector(term,doc.getId(),pos.get(j));
                        docsRepository.addDocVector(doc.getId(),doc.getName(), term);
                    }
                }
            }
            System.out.print("\b\b\b\b\b\b\b\b\b\b\b"+"总进度:"+String.format("%.2f",100.0*(docsRepository.getDocSize()+finish)/num)+"%");
        }
    }

    private void doProcess(String fileName, StrategyType type, String token)
    {
        List data = (ArrayList)reader.get(fileName, ReaderFactory.get(type));
        final int max = 10000;
        int size = data.size();
        int all = (int)Math.ceil(1.0*size/max);
        for(int k=0;k<all;k++)
        {
            int end = max;
            if(end>data.size())
                end = data.size();
            List sub = data.subList(0,end);
            List<Doc> list = converter.process(AnalysisFactory.get(type),sub,token);
            int num = list.size();
            if(list==null)
                continue;

            if(thread<=0)
                doProcess(k*max,size, 0, num, list);
            else
            {
                ExecutorService pool = Executors.newCachedThreadPool();
                int per = num/thread;
                if(per < 1)
                    per = 1;
                for (int s = 0, e = per; e <= num;)
                {
                    int finalS = s, finalE = e;
                    int finalK = k;
                    pool.execute(new Runnable() {
                        @Override
                        public void run()
                        {
                            final int start = finalS,end = finalE;
                            doProcess(finalK *max,size, start, end, list);
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

            System.out.println();
            docsRepository.pushDB(thread,0);
            docsRepository.pushDB(thread,1);
            docsRepository.pushDB(thread,2);
            data = data.subList(end,data.size());
        }
    }

    public PreProcessingInterface getPreProcessing()
    {
        return new PreProcessing(stopWords,BremoveStopWords,Bstem,pattern);
    }

    public DocInterface getDoc(String id)
    {
        return docsRepository.getDoc(id);
    }

    public List<DocVectorInterface> dvsDBfind(int pageNo, int pageSize,int all)
    {
        List list = docsRepository.dvsDBfindCache(pageNo,pageSize,all);
        return (List<DocVectorInterface>)list;
    }

    public ConcurrentHashMap<String,DocInterface> docDBfind(int pageNo, int pageSize,int all)
    {
        ConcurrentHashMap map = docsRepository.docDBfindCache(pageNo,pageSize,all);
        return (ConcurrentHashMap<String,DocInterface>)map;
    }

    public DocVectorInterface getDvByDocid(String docid){return docsRepository.getDvByDocid(docid);}

    public TermVectorInterface getTermByTerm(String term)
    {
        return docsRepository.getTermByTerm(term);
    }

    public long getDocNum()
    {
        return docsRepository.getDocNum();
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
