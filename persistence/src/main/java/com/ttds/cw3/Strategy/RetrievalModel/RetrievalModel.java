package com.ttds.cw3.Strategy.RetrievalModel;

import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.ModelManagerAdapter;
import com.ttds.cw3.Adapter.PreProcessingAdapter;
import com.ttds.cw3.Data.SearchResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class RetrievalModel
{
    protected int thread = 3;

    public RetrievalModel(int thread) {
        this.thread = thread;
    }

    public SearchResult<Double>[] searchAllDoc(String str, String param, PreProcessingAdapter preProcessing, ModelManagerAdapter m, ArrayList<String> filter) throws Exception
    {
        preProcessing.doProcessing(str);
        ConcurrentHashMap<String, ArrayList<Integer>> wordsMap = preProcessing.getTerms();

        // 搜索输入
        ArrayList<String> words = new ArrayList(wordsMap.keySet());

        if(words.size()==0)
            words = new ArrayList(Arrays.asList(str.split(" ")));

        // 结果输出
        List dvs = m.getDvs();
        int size = dvs.size();
        SearchResult<Double>[] results = new SearchResult[size];

        // =============== 准备工作 ===============

        String other = prepare(size,dvs,words,m);

        // =============== 遍历文档集合 ===============
        final int max = 100000;
        int all = (int)Math.ceil(1.0*size/max);
        long startTime = System.currentTimeMillis();
        for(int k=0;k<all;k++)
        {
            int num = (k + 1) * max;
            if (num > size)
                num = size;

            if(thread<=0)
                inThread(size,k*max, num, words, param,dvs, results, m,filter,other);
            else
            {
                ExecutorService pool = Executors.newCachedThreadPool();
                int per = num/thread;
                if(per < 1)
                    per = 1;
                for (int s = 0, e = per; e <= num;)
                {
                    ArrayList<String> finalWords = words;
                    int finalS = s, finalE = e;
                    pool.execute(new Runnable() {
                        @Override
                        public void run()
                        {
                            final int start = finalS,end = finalE;
                            inThread(size,start, end, finalWords, param,dvs, results, m,filter,other);
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
        }


        long endTime = System.currentTimeMillis();
        System.out.println("Retrieval搜索时间："+(endTime-startTime));

        return results;
    }

    protected void inThread(int size, int start, int end, ArrayList<String> words, String param, List dvs,
                            SearchResult<Double>[] results, ModelManagerAdapter m, ArrayList<String> filter,String other)
    {
        for(int j=start;j<end;j++)
        {
            DocVectorAdapter dv = new DocVectorAdapter(dvs.get(j));
            if(dv==null)
                continue;

            // =============== 筛选步骤 ===============
            if(filter!=null && !filter.contains(dv.getDocid()))
            {
                results[j] = new SearchResult(dv.getDocid(),dv.getDocName(), 0.0);
                continue;
            }

            try {
                setParams(param);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            results[j] = searchDoc(words,dv,m.getDoc(dv.getDocid()),m,other);
        }
    }

    public SearchResult<Double> searchDoc(String str,String param, PreProcessingAdapter preProcessing, DocVectorAdapter doc,DocAdapter docinfo, ModelManagerAdapter m, String other) throws Exception
    {
        preProcessing.doProcessing(str);
        ConcurrentHashMap<String, ArrayList<Integer>> wordsMap = preProcessing.getTerms();

        // 搜索输入
        ArrayList<String> words = new ArrayList(wordsMap.keySet());

        if(words.size()==0)
            words = new ArrayList(Arrays.asList(str.split(" ")));

        setParams(param);
        return searchDoc(words,doc,docinfo,m,other);
    }

    protected abstract String prepare(int num, List docs, ArrayList<String> words,ModelManagerAdapter m);

    protected abstract void setParams(String param) throws Exception;

    protected abstract SearchResult<Double> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DocAdapter docinfo, ModelManagerAdapter m,String other);
}
