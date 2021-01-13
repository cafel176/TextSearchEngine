package com.ttds.cw3.Transaction;

import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Factory.AnalysisFactory;
import com.ttds.cw3.Factory.ReaderFactory;
import com.ttds.cw3.Interface.DataInterface;
import com.ttds.cw3.Interface.DocInterface;
import com.ttds.cw3.Interface.ModelManagerInterface;
import com.ttds.cw3.Interface.PreProcessingInterface;
import com.ttds.cw3.Strategy.StrategyType;
import com.ttds.cw3.Tools.DocAnalysis;
import com.ttds.cw3.Tools.DocReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Repository;

@Repository
public final class ModelManager implements ModelManagerInterface
{
    private String filePath = "assets/";
    private String savePath = "save/";
    private String stopWordFile = "englishST.txt";
    private StrategyType stopWordReadType = StrategyType.txt;
    private ArrayList<String> category = null;

    private PreProcessing preProcessing;
    private DocsRepository docsRepository;
    private DocReader reader;
    private DocAnalysis converter;
    private Properties props;

    private final AtomicInteger idGenerator;

    @Autowired
    public ModelManager()
    {
        idGenerator = new AtomicInteger();

        loadProperties("application.properties");

        initDataAndDoc();

        System.out.printf("Model Manager构造完成。\n");
    }

    public boolean setSavePath()
    {
        if(props==null)
        {
            return false;
        }

        savePath = props.getProperty("savePath").trim();
        docsRepository.setSavePath(savePath);

        return true;
    }

    public boolean setCategory()
    {
        if(props==null)
        {
            return false;
        }

        ArrayList<String> category = new ArrayList<>();
        String[] st = props.getProperty("category").trim().split(",");
        for(int i=0;i<st.length;i++)
            category.add(st[i].trim());

        docsRepository.setCategory(category);
        preProcessing.setCategory(category);
        converter.setCategory(category);

        return true;
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
        long startTime = System.currentTimeMillis();

        String saveFile = "save.txt";
        StrategyType saveType = StrategyType.txt;
        boolean reloadFiles = false;

        String docFile = "doc.txt";
        StrategyType docType = StrategyType.txt;

        if(props!=null)
        {
            String[] st = props.getProperty("saveFile").trim().split("#");
            saveFile = st[0].trim();
            saveType = StrategyType.valueOf(st[1].trim());

            st = props.getProperty("docFile").trim().split("#");
            docFile = st[0].trim();
            docType = StrategyType.valueOf(st[1].trim());

            reloadFiles = Boolean.parseBoolean(props.getProperty("reloadFiles").trim());
        }

        if(reloadFiles || !loadData(saveFile,category, saveType) || !loadDoc(docFile,category, docType))
        {
            if(props!=null)
            {
                clearDataAndDoc();
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

                saveData(saveFile,category,saveType);
                saveDoc(docFile,category,docType);
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("数据加载时间："+(endTime-startTime));
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
            savePath = props.getProperty("savePath").trim();

            String[] st = props.getProperty("stopWordFile").trim().split("#");
            stopWordFile = st[0].trim();
            stopWordReadType = StrategyType.valueOf(st[1].trim());

            category = new ArrayList<>();
            st = props.getProperty("category").trim().split(",");
            for(int i=0;i<st.length;i++)
                category.add(st[i].trim());

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

        preProcessing = new PreProcessing(filePath,category);
        preProcessing.setStopWordFile(stopWordFile, stopWordReadType);
        preProcessing.setBremoveStopWords(removeStopWords);
        preProcessing.setBstem(stem);
        preProcessing.setPattern(pattern);

        docsRepository = new DocsRepository(savePath, category);
        reader = new DocReader(filePath);
        converter = new DocAnalysis(category);

        return success;
    }

    private void doProcess(Doc doc)
    {
        String docid = doc.getId();
        String text = doc.getText();

        preProcessing.doProcessing(text);
        HashMap<String,ArrayList<Integer>> tokens = preProcessing.getTerms();

        Iterator iter = tokens.entrySet().iterator();
        while (iter.hasNext())
        {
            HashMap.Entry entry = (HashMap.Entry) iter.next();
            String term = (String)entry.getKey();
            ArrayList<Integer> pos = (ArrayList<Integer>)entry.getValue();

            for(int i=0;i<pos.size();i++)
            {
                docsRepository.addTermVector(term,docid,pos.get(i));
                docsRepository.addDocVector(docid,term);
            }
        }

        docsRepository.addDoc(doc);
    }

    private boolean loadData(String fileName, ArrayList<String> category, StrategyType type)
    {
        return docsRepository.loadData(fileName,category,type);
    }

    private boolean loadDoc(String fileName, ArrayList<String> category, StrategyType type)
    {
        return docsRepository.loadDoc(fileName,category,type);
    }

    private void saveData(String fileName, ArrayList<String> category, StrategyType type)
    {
        docsRepository.saveData(fileName,category, type);
    }

    private void saveDoc(String fileName, ArrayList<String> category, StrategyType type)
    {
        docsRepository.saveDoc(fileName,category, type);
    }

    private void doProcess(String fileName, StrategyType type, String token)
    {
        ArrayList<Doc> list = converter.process(AnalysisFactory.get(type),reader.get(fileName, ReaderFactory.get(type)),token);
        if(list==null)
            return;

        for(int i=0;i<list.size();i++)
        {
            Doc doc = list.get(i);
            if(doc.getId().isEmpty())
            {
                int id = idGenerator.incrementAndGet();
                while(docsRepository.containDoc(Integer.toString(id)))
                {
                    id = idGenerator.incrementAndGet();
                }
                doc.setId(Integer.toString(id));
            }

            doProcess(doc);
        }
    }

    public void clearDataAndDoc()
    {
        docsRepository.clearDataAndDoc();
    }

    public DataInterface getData() {
        return docsRepository.getData();
    }

    public PreProcessingInterface getPreProcessing()
    {
        return preProcessing;
    }

    public DocInterface getDoc(String id)
    {
        return docsRepository.getDoc(id);
    }
}
