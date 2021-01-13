package com.ttds.cw3.Tools;

import com.ttds.cw3.Adapter.DataAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.PreProcessingAdapter;
import com.ttds.cw3.Data.SearchResult;
import com.ttds.cw3.Factory.RetrievalModelFactory;
import com.ttds.cw3.Strategy.RetrievalModel.RetrievalModel;
import com.ttds.cw3.Strategy.RetrievalModel.RetrievalModelType;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public final class RetrievalDriver
{
    private DataAdapter data;

    private PreProcessingAdapter preProcessing;

    public RetrievalDriver(DataAdapter data, PreProcessingAdapter preProcessing) {
        this.data = data;
        this.preProcessing = preProcessing;
    }

    public void setData(DataAdapter data) {
        this.data = data;
    }

    public void setPreProcessing(PreProcessingAdapter preProcessing) {
        this.preProcessing = preProcessing;
    }

    public ArrayList<Pair<String, Double>> spaenDatas(RetrievalModelType type, String txt, String param) throws Exception
    {
        RetrievalModel module = RetrievalModelFactory.get(type);
        SearchResult<Double>[] results =  search(txt,param,module);
        ArrayList<Pair<String, Double>> arr = new ArrayList(Arrays.asList((results)));
        return arr;
    }

    public ArrayList<SearchResult<Double>> sort(ArrayList<SearchResult<Double>> arr)
    {
        ArrayList<SearchResult<Double>> results = new ArrayList<>();
        for(int i=0;i<arr.size();i++)
        {
            if(arr.get(i).getValue()>0.0001)
            {
                results.add(arr.get(i));
            }
        }

        Collections.sort(results ,new Comparator<SearchResult<Double>>()
        {
            @Override
            public int compare(SearchResult<Double> s1, SearchResult<Double> s2)
            {
                int flag;
                flag = Double.compare(s2.getValue(),s1.getValue());

                if(Math.abs(flag)<0.0001){
                    flag = (int)(Double.parseDouble(s2.getDocid())-Double.parseDouble(s1.getDocid()));
                }

                return flag;
            }
        });

        return results;
    }

    private SearchResult<Double>[] search(String str,String param, RetrievalModel module) throws Exception
    {
        if(module==null)
        {
            System.out.println("RetrievalModel加载出错！");
            return null;
        }

        return module.searchAllDoc(str,param,data,preProcessing);
    }

    private SearchResult<Double> search(String str,String param, RetrievalModel module, DocVectorAdapter doc) throws Exception
    {
        if(module==null)
        {
            System.out.println("RetrievalModel加载出错！");
            return null;
        }

        return module.searchDoc(str,param,data,preProcessing,doc);
    }
}
