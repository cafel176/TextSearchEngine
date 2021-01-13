package com.ttds.cw3.Tools;

import com.ttds.cw3.Adapter.*;
import com.ttds.cw3.Data.SearchResult;
import com.ttds.cw3.Factory.SearchModuleFactory;
import com.ttds.cw3.Strategy.SearchModule.SearchModule;
import com.ttds.cw3.Strategy.SearchModule.SearchModuleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public final class SearchDriver
{
    private DataAdapter data;

    private int limit;
    private String pattern;

    private PreProcessingAdapter preProcessing;

    public SearchDriver(DataAdapter data, PreProcessingAdapter preProcessing,int limit,String pattern)
    {
        this.limit = limit;
        this.pattern = pattern;
        this.data = data;
        this.preProcessing = preProcessing;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setData(DataAdapter data) {
        this.data = data;
    }

    public void setPreProcessing(PreProcessingAdapter preProcessing) {
        this.preProcessing = preProcessing;
    }

    public ArrayList<SearchResult<Boolean>> spaenDatas(SearchModuleType type, String txt,String param, ModelManagerAdapter m) throws Exception
    {
        SearchModule module = SearchModuleFactory.get(type,limit,pattern);
        SearchResult<Boolean>[] results =  search(txt,param,module,m);
        ArrayList<SearchResult<Boolean>> arr = new ArrayList(Arrays.asList((results)));
        return arr;
    }

    public ArrayList<SearchResult<Boolean>> sort(ArrayList<SearchResult<Boolean>> arr)
    {
        ArrayList<SearchResult<Boolean>> results = new ArrayList<>();
        for(int i=0;i<arr.size();i++)
        {
            if(arr.get(i).getValue())
            {
                results.add(arr.get(i));
            }
        }

        Collections.sort(results ,new Comparator<SearchResult<Boolean>>()
        {
            @Override
            public int compare(SearchResult<Boolean> s1, SearchResult<Boolean> s2)
            {
                int flag;
                flag = Integer.parseInt(s1.getDocid())-Integer.parseInt(s2.getDocid());
                return flag;
            }
        });

        return results;
    }

    private SearchResult<Boolean>[] search(String str, String param, SearchModule module, ModelManagerAdapter m) throws Exception
    {
        if(module==null)
        {
            System.out.println("SearchModule加载出错！");
            return null;
        }

        return module.searchAllDoc(str,param,data,preProcessing,m);
    }

    private SearchResult<Boolean> search(String str,String param, SearchModule module, DocVectorAdapter doc, DocAdapter docinfo) throws Exception
    {
        if(module==null)
        {
            System.out.println("SearchModule加载出错！");
            return null;
        }

        return module.searchDoc(str,param,data,preProcessing,doc,docinfo);
    }
}
