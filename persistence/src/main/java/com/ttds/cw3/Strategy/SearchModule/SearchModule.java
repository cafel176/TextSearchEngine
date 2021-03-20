package com.ttds.cw3.Strategy.SearchModule;

import com.ttds.cw3.Adapter.*;
import com.ttds.cw3.Data.SearchResult;
import com.ttds.cw3.Interface.DocVectorInterface;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SearchModule
{
    protected int thread = 3;
    protected int limit = 30;
    protected String pattern = "[\\w]+";

    public SearchModule(int limit, int thread, String pattern)
    {
        this.limit = limit;
        this.thread = thread;
        this.pattern = pattern;
    }

    public void setLimit(int limit,int thread) {
        this.limit = limit;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public SearchResult<Boolean>[] searchAllDoc(String str, String param, PreProcessingAdapter preProcessing, ModelManagerAdapter m) throws Exception
    {
        setParams(param);

        preProcessing.doProcessing(str);
        HashMap<String, ArrayList<Integer>> wordsMap = preProcessing.getTerms();

        // 搜索输入
        ArrayList<String> words = new ArrayList(wordsMap.keySet());

        if(words.size()==0)
            words = new ArrayList(Arrays.asList(str.split(" ")));

        // 结果输出
        SearchResult<Boolean>[] results = new SearchResult[m.getDocSize()];

        // =============== 遍历文档集合 ===============
        List docs = m.getDvs();
        List docinfos = m.getDocs();
        int num = docs.size();

        long startTime = System.currentTimeMillis();

        if(thread<=0)
            inThread(num,0, num, words, docs,docinfos,results, m);
        else
        {
            ExecutorService pool = Executors.newCachedThreadPool();
            int per = num/thread;
            for (int s = 0, e = per; e <= num;)
            {
                ArrayList<String> finalWords = words;
                int finalS = s, finalE = e;
                pool.execute(new Runnable() {
                    @Override
                    public void run()
                    {
                        final int start = finalS,end = finalE;
                        inThread(num,start, end, finalWords, docs,docinfos,results, m);
                    }
                });

                if(e==num)
                    break;

                s = e;
                e += per;
                if(e>num)
                    e = num;
            }
            pool.shutdown();
            try
            {
                pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Search搜索时间："+(endTime-startTime));

        return results;
    }

    protected void inThread(int num, int start, int end, ArrayList<String> words, List docs,List docinfos,
                            SearchResult<Boolean>[] results, ModelManagerAdapter m)
    {
        for(int j=start;j<end;j++)
        {
            DocAdapter docinfo = new DocAdapter(docinfos.get(j));
            DocVectorAdapter doc = m.getDvByDocid(docinfo.getId());
            if(doc==null)
                continue;

            // =============== 筛选步骤 ===============
            boolean check = false;
            for (String word : words)
                if(!doc.getTerms().containsKey(word)) // 当前文档不包含查询词
                {
                    results[j] = new SearchResult(doc.getDocid(),doc.getDocName(), false);
                    check = true;
                    break;
                }
            if(check)
                continue;

            results[j] = searchDoc(words,doc,docinfo,m);
        }
    }

    public SearchResult<Boolean> searchDoc(String str, String param, PreProcessingAdapter preProcessing, DocVectorAdapter doc,DocAdapter docinfo, ModelManagerAdapter m) throws Exception
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
                return new SearchResult(doc.getDocid(),doc.getDocName(),false);

        return searchDoc(words,doc,docinfo,m);
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

    protected abstract SearchResult<Boolean> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DocAdapter docinfo, ModelManagerAdapter m);

}
