package com.ttds.cw3.Adapter;

import com.ttds.cw3.Interface.OtherParamsInterface;
import com.ttds.cw3.Interface.PersistanceManagerInterface;
import com.ttds.cw3.Interface.SearchResultInterface;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PersistanceManagerAdapter
{
    private PersistanceManagerInterface manager;

    @Autowired
    public PersistanceManagerAdapter(PersistanceManagerInterface manager)
    {
        this.manager = manager;
    }

    public boolean loadProperties(String name)
    {
        return manager.loadProperties(name);
    }

    public Pair<OtherParamsAdapter,ArrayList<SearchResultAdapter<Double>>> search(String str, int start, int end) throws Exception
    {
        Pair<OtherParamsInterface,ArrayList<SearchResultInterface<Double>>> p = manager.search(str,start,end);
        ArrayList<SearchResultInterface<Double>> arr = p.getValue();
        OtherParamsAdapter param = new OtherParamsAdapter(p.getKey());
        ArrayList<SearchResultAdapter<Double>> re = new ArrayList<>();
        for(int i=0;i<arr.size();i++)
        {
            re.add(new SearchResultAdapter<>(arr.get(i)));
        }
        return new Pair<>(param,re);
    }

    public Pair<OtherParamsAdapter,ArrayList<SearchResultAdapter<Double>>> searchByRetrievalModel(String str, int start, int end) throws Exception
    {
        Pair<OtherParamsInterface,ArrayList<SearchResultInterface<Double>>> p = manager.searchByRetrievalModel(str,start,end);
        ArrayList<SearchResultInterface<Double>> arr = p.getValue();
        OtherParamsAdapter param = new OtherParamsAdapter(p.getKey());
        ArrayList<SearchResultAdapter<Double>> re = new ArrayList<>();
        for(int i=0;i<arr.size();i++)
        {
            re.add(new SearchResultAdapter<>(arr.get(i)));
        }
        return new Pair<>(param,re);
    }

    public Pair<OtherParamsAdapter,ArrayList<SearchResultAdapter<Boolean>>> searchBySearchModule(String str, int start, int end) throws Exception
    {
        Pair<OtherParamsInterface,ArrayList<SearchResultInterface<Boolean>>> p = manager.searchBySearchModule(str,start,end);
        ArrayList<SearchResultInterface<Boolean>> arr = p.getValue();
        OtherParamsAdapter param = new OtherParamsAdapter(p.getKey());
        ArrayList<SearchResultAdapter<Boolean>> re = new ArrayList<>();
        for(int i=0;i<arr.size();i++)
        {
            re.add(new SearchResultAdapter<>(arr.get(i)));
        }
        return new Pair<>(param,re);
    }
}
