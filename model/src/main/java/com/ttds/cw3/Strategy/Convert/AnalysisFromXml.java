package com.ttds.cw3.Strategy.Convert;

import com.ttds.cw3.Data.Data;
import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Data.JSONAdapter;

import java.util.ArrayList;

public final class AnalysisFromXml extends AnalysisStrategy<ArrayList<Doc>>
{
    @Override
    public ArrayList<Doc> process(ArrayList<Doc> data, ArrayList<String> category, String token)
    {
        if(category==null)
            return data;

        ArrayList<Doc> list = new ArrayList<>();
        for(int i=0;i<data.size();i++)
            if(category.contains(data.get(i).getCategory()))
                list.add(data.get(i));

        return list;
    }

    @Override
    public ArrayList<String> convertTxtList(ArrayList<Doc> data)
    {
        if(data==null)
            return null;

        ArrayList<String> list = new ArrayList<>();
        for(int i=0;i<data.size();i++)
        {
            list.add(data.get(i).getText());
        }

        return list;
    }

    @Override
    public Data load(ArrayList<Doc> t)
    {
        if(t==null)
            return null;

        return null;
    }

    @Override
    public ArrayList<Doc> save(Data data)
    {
        if(data==null)
            return null;

        return null;
    }

    @Override
    public ArrayList<Doc> saveDocs(ArrayList<Doc> docs)
    {
        if(docs==null)
            return null;

        return docs;
    }

    @Override
    public ArrayList<Doc> loadDocs(ArrayList<Doc> docs)
    {
        if(docs==null)
            return null;

        return docs;
    }
}
