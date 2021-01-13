package com.ttds.cw3.Strategy.SearchModule;

import com.ttds.cw3.Adapter.*;
import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SearchModule
{
    protected int limit = 30;
    protected String pattern = "[\\w]+";

    public SearchModule(int limit, String pattern)
    {
        this.limit = limit;
        this.pattern = pattern;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public SearchResult<Boolean>[] searchAllDoc(String str, String param, DataAdapter data, PreProcessingAdapter preProcessing, ModelManagerAdapter m) throws Exception
    {
        setParams(param);

        preProcessing.doProcessing(str);
        HashMap<String, ArrayList<Integer>> wordsMap = preProcessing.getTerms();

        // 搜索输入
        ArrayList<String> words = new ArrayList(wordsMap.keySet());

        if(words.size()==0)
            words = new ArrayList(Arrays.asList(str.split(" ")));

        // terms集合
        ArrayList<String> terms = data.getTermKeys();
        // 结果输出
        SearchResult<Boolean>[] results = new SearchResult[data.getDvsSize()];

        // =============== 遍历文档集合 ===============
        Iterator iter = data.getDvsIterator();
        int i = -1;
        while (iter.hasNext())
        {
            i++;
            HashMap.Entry entry = (HashMap.Entry) iter.next();
            DocVectorAdapter doc = new DocVectorAdapter(entry.getValue());
            DocAdapter docinfo = m.getDoc(doc.getDocid());

            // =============== 筛选步骤 ===============
            boolean check = false;
            for (String word : words)
                if(!doc.getTerms().containsKey(word)) // 当前文档不包含查询词
                {
                    results[i] = new SearchResult(doc.getDocid(),false);
                    check = true;
                    break;
                }
            if(check)
                continue;

            results[i] = searchDoc(words,doc,data,docinfo);
        }

        return results;
    }

    public SearchResult<Boolean> searchDoc(String str, String param, DataAdapter data, PreProcessingAdapter preProcessing, DocVectorAdapter doc,DocAdapter docinfo) throws Exception
    {
        setParams(param);

        preProcessing.doProcessing(str);
        HashMap<String, ArrayList<Integer>> wordsMap = preProcessing.getTerms();

        // 搜索输入
        ArrayList<String> words = new ArrayList(wordsMap.keySet());

        if(words.size()==0)
            words = new ArrayList(Arrays.asList(str.split(" ")));

        // =============== 筛选步骤 2 ===============
        for (String word : words)
            if(!doc.getTerms().containsKey(word)) // 当前文档不包含查询词
                return new SearchResult(doc.getDocid(),false);

        return searchDoc(words,doc,data,docinfo);
    }

    protected String getRelatedStr(int pos, String text, String pattern)
    {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        int i = 0;
        String find="";
        while (m.find())
        {
            if(i==pos)
            {
                find = m.group();
                break;
            }
            i++;
        }

        if(find.isEmpty())
            return text;

        pos = text.indexOf(find);
        int start = pos-limit/2;
        int end = pos+limit/2;
        int length = text.length();
        if(start<0)
        {
            start = 0;
            end = limit>length?length:limit;
        }
        if(end>length)
        {
            end = length;
            start = length-limit<0?0:length-limit;
        }

        int _start = text.indexOf(" ",start);
        if(_start!=-1 && _start<pos)
            start = _start;

        int _end = text.indexOf(" ",end);
        if(_end!=-1)
            end = _end;

        return text.substring(start,end);
    }

    protected abstract void setParams(String param) throws Exception;

    protected abstract SearchResult<Boolean> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DataAdapter data,DocAdapter docinfo);

}
