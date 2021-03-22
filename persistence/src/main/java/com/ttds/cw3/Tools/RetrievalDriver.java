package com.ttds.cw3.Tools;

import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.ModelManagerAdapter;
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
    private int thread;
    private ModelManagerAdapter m;

    private PreProcessingAdapter preProcessing;

    public RetrievalDriver(ModelManagerAdapter m, PreProcessingAdapter preProcessing,int thread) {
        this.m = m;
        this.thread = thread;
        this.preProcessing = preProcessing;
    }

    public void setPreProcessing(PreProcessingAdapter preProcessing) {
        this.preProcessing = preProcessing;
    }

    public ArrayList<SearchResult<Double>> spaenDatas(RetrievalModelType type, String txt, String param) throws Exception
    {
        return spaenDatas(type, txt, param,null);
    }

    public ArrayList<SearchResult<Double>> spaenDatas(RetrievalModelType type, String txt, String param, ArrayList<String> filter) throws Exception
    {
        RetrievalModel module = RetrievalModelFactory.get(thread,type);
        SearchResult<Double>[] results =  search(txt,param,module,filter);
        return new ArrayList(Arrays.asList((results)));
    }

    public ArrayList<SearchResult<Double>> clip(ArrayList<SearchResult<Double>> arr)
    {
        ArrayList<SearchResult<Double>> results = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            SearchResult<Double> a = arr.get(i);
            if (a != null && a.getValue() > 0.0001) {
                results.add(a);
            }
        }
        return results;
    }

    public ArrayList<SearchResult<Double>> sort(ArrayList<SearchResult<Double>> arr)
    {
        ArrayList<SearchResult<Double>> results = arr;
        Collections.sort(results ,new Comparator<SearchResult<Double>>()
        {
            @Override
            public int compare(SearchResult<Double> s1, SearchResult<Double> s2)
            {
                int flag;
                flag = Double.compare(s2.getValue(),s1.getValue());

                if(flag==0){
                    String n1 = s1.getDocName();
                    String n2 = s2.getDocName();
                    if(n1==null||n1.isEmpty())
                        n1 = s1.getDocid();
                    if(n2==null||n2.isEmpty())
                        n2 = s2.getDocid();
                    flag = Integer.parseInt(n2)-Integer.parseInt(n1);
                }

                return flag;
            }
        });

        return results;
    }

    private SearchResult<Double>[] search(String str,String param, RetrievalModel module, ArrayList<String> filter) throws Exception
    {
        if(module==null)
        {
            System.out.println("RetrievalModel加载出错！");
            return null;
        }

        return module.searchAllDoc(str,param,preProcessing,m,filter);
    }

    private SearchResult<Double> search(String str, String param, RetrievalModel module, DocVectorAdapter doc, DocAdapter docinfo,String other) throws Exception
    {
        if(module==null)
        {
            System.out.println("RetrievalModel加载出错！");
            return null;
        }

        return module.searchDoc(str,param,preProcessing,doc,docinfo,m,other);
    }
}
