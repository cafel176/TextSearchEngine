package com.ttds.cw3.Transaction;

import com.ttds.cw3.Data.Data;
import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Factory.AnalysisFactory;
import com.ttds.cw3.Factory.ReaderFactory;
import com.ttds.cw3.Factory.WriterFactory;
import com.ttds.cw3.Strategy.StrategyType;
import com.ttds.cw3.Tools.DocAnalysis;
import com.ttds.cw3.Tools.DocReader;
import com.ttds.cw3.Tools.DocWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class DocsRepository
{
    private DocWriter writer;
    private DocReader reader;
    private DocAnalysis converter;

    private Data data;
    private HashMap<String,Doc> docList;

    public DocsRepository(String savePath, ArrayList<String> category)
    {
        data = new Data();
        writer = new DocWriter(savePath);
        reader = new DocReader(savePath);
        converter = new DocAnalysis(category);
        docList = new HashMap<>();
    }

    public void setSavePath(String file)
    {
        writer.setFilePath(file);
    }

    public void setCategory(ArrayList<String> category)
    {
        converter.setCategory(category);
    }

    public void addDocVector(String docid, String term)
    {
        data.addDocVector(docid,term);
    }

    public void addTermVector(String term, String docid, int pos)
    {
        data.addTermVector(term,docid,pos);
    }

    public Data getData() {
        return data;
    }

    public void clearDataAndDoc()
    {
        data.clear();
        docList.clear();
    }

    public void addDoc(Doc doc)
    {
        docList.put(doc.getId(),doc);
    }

    public Doc getDoc(String id)
    {
        return docList.get(id);
    }

    public boolean containDoc(String id)
    {
        return docList.containsKey(id);
    }

    public void saveData(String fileName, ArrayList<String> category, StrategyType type)
    {
        String before = getCategoryBefore(category);

        writer.write(before+fileName, WriterFactory.get(type), converter.save(AnalysisFactory.get(type), data));
    }

    public boolean loadData(String fileName, ArrayList<String> category, StrategyType type)
    {
        String before = getCategoryBefore(category);

        Data _data = converter.load(AnalysisFactory.get(type), reader.get(before+fileName, ReaderFactory.get(type)));

        if(_data!=null)
        {
            data = _data;
            return true;
        }
        else
            return false;
    }

    public void saveDoc(String fileName, ArrayList<String> category, StrategyType type)
    {
        String before = getCategoryBefore(category);

        Iterator iter = docList.entrySet().iterator();
        ArrayList<Doc> list = new ArrayList<>();
        while (iter.hasNext())
        {
            HashMap.Entry entry = (HashMap.Entry) iter.next();
            list.add((Doc)entry.getValue());
        }

        writer.write(before+fileName, WriterFactory.get(type), converter.saveDocs(AnalysisFactory.get(type), list));
    }

    public boolean loadDoc(String fileName, ArrayList<String> category, StrategyType type)
    {
        String before = getCategoryBefore(category);

        ArrayList<Doc> _data = converter.loadDocs(AnalysisFactory.get(type), reader.get(before+fileName, ReaderFactory.get(type)));

        if(_data!=null)
        {
            for(int i=0;i< _data.size();i++)
            {
                Doc doc = _data.get(i);
                docList.put(doc.getId(),doc);
            }
            return true;
        }
        else
            return false;
    }

    private String getCategoryBefore(ArrayList<String> category)
    {
        String before = "";
        if(category != null)
            for(int i=0;i<category.size();i++)
            {
                String c = category.get(i);
                if(!c.isEmpty())
                    before += c + "_";
            }
        return before;
    }
}
