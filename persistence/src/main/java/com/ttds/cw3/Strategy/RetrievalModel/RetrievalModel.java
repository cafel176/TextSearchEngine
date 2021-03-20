package com.ttds.cw3.Strategy.RetrievalModel;

import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.ModelManagerAdapter;
import com.ttds.cw3.Adapter.PreProcessingAdapter;
import com.ttds.cw3.Data.SearchResult;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class RetrievalModel
{
    protected int thread = 3;

    public RetrievalModel(int thread) {
        this.thread = thread;
    }

    public SearchResult<Double>[] searchAllDoc(String str, String param, PreProcessingAdapter preProcessing, ModelManagerAdapter m) throws Exception
    {
        preProcessing.doProcessing(str);
        HashMap<String, ArrayList<Integer>> wordsMap = preProcessing.getTerms();

        // 搜索输入
        ArrayList<String> words = new ArrayList(wordsMap.keySet());

        if(words.size()==0)
            words = new ArrayList(Arrays.asList(str.split(" ")));

        // 结果输出
        SearchResult<Double>[] results = new SearchResult[m.getDocSize()];

        // =============== 准备工作 ===============
        List docs = m.getDvs();
        List docinfos = m.getDocs();
        int num = docs.size();

        String other = prepare(num,docs,docinfos,words);

        // =============== 遍历文档集合 ===============
        long startTime = System.currentTimeMillis();

        if(thread<=0)
            inThread(num,0, num, words, param,docs,docinfos, results, m,other);
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
                        inThread(num,start, end, finalWords, param,docs,docinfos, results, m,other);
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
        System.out.println("Retrieval搜索时间："+(endTime-startTime));

        return results;
    }

    protected void inThread(int num, int start, int end, ArrayList<String> words, String param,
                            List docs,List docinfos, SearchResult<Double>[] results, ModelManagerAdapter m,String other)
    {
        for(int j=start;j<end;j++)
        {
            DocAdapter docinfo = new DocAdapter(docinfos.get(j));
            DocVectorAdapter doc = m.getDvByDocid(docinfo.getId());
            if(doc==null)
                continue;

            try {
                setParams(param);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            results[j] = searchDoc(words,doc,docinfo,m,other);
        }
    }

    public SearchResult<Double> searchDoc(String str,String param, PreProcessingAdapter preProcessing, DocVectorAdapter doc,DocAdapter docinfo, ModelManagerAdapter m, String other) throws Exception
    {
        preProcessing.doProcessing(str);
        HashMap<String, ArrayList<Integer>> wordsMap = preProcessing.getTerms();

        // 搜索输入
        ArrayList<String> words = new ArrayList(wordsMap.keySet());

        if(words.size()==0)
            words = new ArrayList(Arrays.asList(str.split(" ")));

        setParams(param);
        return searchDoc(words,doc,docinfo,m,other);
    }

    protected abstract String prepare(int num, List docs,List docinfos, ArrayList<String> words);

    protected abstract void setParams(String param) throws Exception;

    protected abstract SearchResult<Double> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DocAdapter docinfo, ModelManagerAdapter m,String other);
}
