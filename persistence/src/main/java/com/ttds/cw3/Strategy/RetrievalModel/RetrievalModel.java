package com.ttds.cw3.Strategy.RetrievalModel;

import com.ttds.cw3.Adapter.DataAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.PreProcessingAdapter;
import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public abstract class RetrievalModel
{
    public SearchResult<Double>[] searchAllDoc(String str, String param, DataAdapter data, PreProcessingAdapter preProcessing) throws Exception
    {
        preProcessing.doProcessing(str);
        HashMap<String, ArrayList<Integer>> wordsMap = preProcessing.getTerms();

        // 搜索输入
        ArrayList<String> words = new ArrayList(wordsMap.keySet());

        if(words.size()==0)
            words = new ArrayList(Arrays.asList(str.split(" ")));

        // 结果输出
        SearchResult<Double>[] results = new SearchResult[data.getDvsSize()];

        // =============== 遍历文档集合 ===============
        Iterator iter = data.getDvsIterator();
        int i = -1;
        while (iter.hasNext())
        {
            i++;
            HashMap.Entry entry = (HashMap.Entry) iter.next();
            DocVectorAdapter doc = new DocVectorAdapter(entry.getValue());

            setParams(param);
            results[i] = searchDoc(words,doc,data);
        }

        return results;
    }

    public SearchResult<Double> searchDoc(String str,String param, DataAdapter data, PreProcessingAdapter preProcessing, DocVectorAdapter doc) throws Exception
    {
        preProcessing.doProcessing(str);
        HashMap<String, ArrayList<Integer>> wordsMap = preProcessing.getTerms();

        // 搜索输入
        ArrayList<String> words = new ArrayList(wordsMap.keySet());

        if(words.size()==0)
            words = new ArrayList(Arrays.asList(str.split(" ")));

        setParams(param);
        return searchDoc(words,doc,data);
    }

    protected abstract void setParams(String param) throws Exception;

    protected abstract SearchResult<Double> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DataAdapter data);
}
