package com.ttds.cw3.Tools;

import com.ttds.cw3.Data.Data;
import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Strategy.Convert.AnalysisStrategy;

import java.util.ArrayList;

public final class DocAnalysis
{
    private ArrayList<String> category;

    public DocAnalysis(ArrayList<String> category) {
        this.category = category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public Object saveDocs(AnalysisStrategy converter, ArrayList<Doc> docs)
    {
        if(converter==null)
        {
            System.out.println("Converter加载出错！");
            return null;
        }
        return converter.saveDocs(docs);
    }

    public ArrayList<Doc> loadDocs(AnalysisStrategy converter,  Object data)
    {
        if(converter==null)
        {
            System.out.println("Converter加载出错！");
            return null;
        }
        return converter.loadDocs(data);
    }

    public Data load(AnalysisStrategy converter, Object t)
    {
        if(converter==null)
        {
            System.out.println("Converter加载出错！");
            return null;
        }
        return converter.load(t);
    }

    public Object save(AnalysisStrategy converter, Data data)
    {
        if(converter==null)
        {
            System.out.println("Converter加载出错！");
            return null;
        }
        return converter.save(data);
    }

    public ArrayList<Doc> process(AnalysisStrategy converter, Object data, String token)
    {
        if(converter==null)
        {
            System.out.println("Converter加载出错！");
            return null;
        }
        return converter.process(data,category,token);
    }

    public ArrayList<String> txtsfrom(AnalysisStrategy converter, Object data)
    {
        if(converter==null)
        {
            System.out.println("Converter加载出错！");
            return null;
        }
        return converter.convertTxtList(data);
    }
}
