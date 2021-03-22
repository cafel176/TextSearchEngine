package com.ttds.cw3.Strategy.RetrievalModel;

import com.ttds.cw3.Adapter.*;
import com.ttds.cw3.Data.SearchResult;
import com.ttds.cw3.Interface.DocInterface;

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

    public SearchResult<Double>[] searchAllDoc(int max,String str, String param, PreProcessingAdapter preProcessing, ModelManagerAdapter m, ArrayList<String> filter) throws Exception
    {
        preProcessing.doProcessing(str);
        ConcurrentHashMap<String, ArrayList<Integer>> wordsMap = preProcessing.getTerms();

        // 搜索输入
        ArrayList<String> words = new ArrayList(wordsMap.keySet());

        if(words.size()==0)
            words = new ArrayList(Arrays.asList(str.split(" ")));

        ArrayList<TermVectorAdapter> tvs = new ArrayList<>();
        for(int i=0;i< words.size();i++)
            tvs.add(m.getTermByTerm(words.get(i)));

        // 结果输出
        int size = m.getDocSize();
        SearchResult<Double>[] results = new SearchResult[size];

        int all = (int)Math.ceil(1.0*size/max);

        long startTime = System.currentTimeMillis();
        // =============== 准备工作 ===============
        prepare(size, max,all,words,m);
        String other = Integer.toString(size);

        // =============== 遍历文档集合 ===============
        for(int k=0;k<all;k++)
        {
            int num = (k + 1) * max;
            if (num > size)
                num = size;

            List dvs = null;
            ConcurrentHashMap<String, DocInterface> docs = null;
            while (docs==null)
                docs = m.docDBfind(k,max,all);
            while (dvs==null)
                dvs = m.dvsDBfind(k,max,all);

            if(thread<=0)
                inThread(k*max,k*max, num, tvs, param, results,dvs,docs, m,filter,other);
            else
            {
                ExecutorService pool = Executors.newCachedThreadPool();
                int per = max/thread;
                if(per < 1)
                    per = 1;
                for (int s = k*max, e = k*max+per; e <= num;)
                {
                    ArrayList<TermVectorAdapter> finalWords = tvs;
                    int finalS = s, finalE = e;
                    List finalDvs = dvs;
                    ConcurrentHashMap<String,DocInterface> finalDocs = docs;
                    int finalK = k;
                    pool.execute(new Runnable() {
                        @Override
                        public void run()
                        {
                            final int start = finalS,end = finalE;
                            inThread(finalK *max,start, end, finalWords, param, results, finalDvs, finalDocs, m,filter,other);
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

    protected void inThread(int ori, int start, int end, ArrayList<TermVectorAdapter> tvs, String param,
                            SearchResult<Double>[] results, List dvs,ConcurrentHashMap<String,DocInterface> docs,ModelManagerAdapter m, ArrayList<String> filter,String other)
    {
        for(int j=start;j<end;j++)
        {
            DocVectorAdapter dv = new DocVectorAdapter(dvs.get(j-ori));

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

            Object doc = docs.get(dv.getDocid());
            if(doc==null)
                continue;

            results[j] = searchDoc(tvs,dv,new DocAdapter(doc),other);
        }
    }

    protected abstract String prepare(int size, int max,int all, ArrayList<String> words,ModelManagerAdapter m);

    protected abstract void setParams(String param) throws Exception;

    protected abstract SearchResult<Double> searchDoc(ArrayList<TermVectorAdapter> words, DocVectorAdapter doc, DocAdapter docinfo, String other);
}
