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
    private ModelManagerAdapter m;
    private int limit;
    private int thread;
    private String pattern;

    private PreProcessingAdapter preProcessing;

    public SearchDriver(ModelManagerAdapter m, PreProcessingAdapter preProcessing,int limit,int thread,String pattern)
    {
        this.limit = limit;
        this.pattern = pattern;
        this.m = m;
        this.thread = thread;
        this.preProcessing = preProcessing;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setPreProcessing(PreProcessingAdapter preProcessing) {
        this.preProcessing = preProcessing;
    }

    public ArrayList<SearchResult<Boolean>> spaenDatas(SearchModuleType type, String txt,String param, ModelManagerAdapter m) throws Exception
    {
        SearchModule module = SearchModuleFactory.get(type,limit,thread,pattern);
        SearchResult<Boolean>[] results =  search(txt,param,module,m);
        ArrayList<SearchResult<Boolean>> arr = new ArrayList(Arrays.asList((results)));
        return arr;
    }

    public ArrayList<SearchResult<Boolean>> clip(ArrayList<SearchResult<Boolean>> arr)
    {
        ArrayList<SearchResult<Boolean>> results = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            SearchResult<Boolean> a = arr.get(i);
            if (a != null && a.getValue()) {
                results.add(a);
            }
        }
        return results;
    }

    public ArrayList<SearchResult<Boolean>> sort(ArrayList<SearchResult<Boolean>> arr)
    {
        ArrayList<SearchResult<Boolean>> results = arr;
        Collections.sort(results ,new Comparator<SearchResult<Boolean>>()
        {
            @Override
            public int compare(SearchResult<Boolean> s1, SearchResult<Boolean> s2)
            {
                int flag;
                String n1 = s1.getDocName();
                String n2 = s2.getDocName();
                if(n1==null||n1.isEmpty())
                    n1 = s1.getDocid();
                if(n2==null||n2.isEmpty())
                    n2 = s2.getDocid();
                flag = Integer.parseInt(n1)-Integer.parseInt(n2);
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

        return module.searchAllDoc(str,param,preProcessing,m);
    }

    private SearchResult<Boolean> search(String str,String param, SearchModule module, DocVectorAdapter doc, DocAdapter docinfo) throws Exception
    {
        if(module==null)
        {
            System.out.println("SearchModule加载出错！");
            return null;
        }

        return module.searchDoc(str,param,preProcessing,doc,docinfo,m);
    }
}
