package com.ttds.cw3.Strategy.RetrievalModel;

import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.ModelManagerAdapter;
import com.ttds.cw3.Adapter.TermVectorAdapter;
import com.ttds.cw3.Data.SearchResult;
import com.ttds.cw3.Interface.DocInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class BM25Model extends RetrievalModel
{
    private double alldl = 0.0;
    private double N = 0.0;

    public BM25Model(int thread) {
        super(thread);
    }

    @Override
    protected SearchResult<Double> searchDoc(ArrayList<TermVectorAdapter> terms, DocVectorAdapter doc, DocAdapter docinfo, String other)
    {
        double N = Integer.parseInt(other);
        double score = 0.0;
        double k = 1.2;
        double b = 0.75;
        double avgdl = this.alldl/this.N;

        for(int i=0;i<terms.size();i++)
        {
            Integer tf = doc.getTerms().get(terms.get(i).getTerm());
            if(tf==null)
                continue;
            double dl = docinfo.getText().length();
            double l = dl/avgdl;
            double df = terms.get(i).getDf();
            double re = ((tf*(k+1))/(k*(1.0-b+b*l)+tf)) * log((N-df+0.5)/(df+0.5),10);
            score += re;
        }

        SearchResult re = new SearchResult(doc.getDocid(),doc.getDocName(), score);
        re.setDesc(Double.toString(score));

        return re;
    }

    @Override
    protected void setParams(String param) throws Exception
    {

    }

    @Override
    protected String prepare(int size, int max,int all, ArrayList<String> words,ModelManagerAdapter m)
    {
        for(int k=0;k<all;k++)
        {
            int num = (k + 1) * max;
            if (num > size)
                num = size;

            List dvs = null;
            ConcurrentHashMap<String, DocInterface>  docs = null;
            while (docs == null)
                docs = m.docDBfind(k, max, all);
            while (dvs == null)
                dvs = m.dvsDBfind(k, max, all);

            if(thread<=0)
                process(k*max,k*max, num, words,dvs,docs);
            else
            {
                ExecutorService pool = Executors.newCachedThreadPool();
                int per = max/thread;
                if(per < 1)
                    per = 1;
                for (int s = k*max, e = k*max+per; e <= num;)
                {
                    ArrayList<String> finalWords = words;
                    int finalS = s, finalE = e;
                    int finalK = k;
                    List finalDvs = dvs;
                    ConcurrentHashMap<String,DocInterface> finalDocs = docs;
                    pool.execute(new Runnable() {
                        @Override
                        public void run()
                        {
                            final int start = finalS,end = finalE;
                            process(finalK *max,start, end, finalWords, finalDvs, finalDocs);
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

        return null;
    }

    private void process(int ori, int start,int end,ArrayList<String> words,List dvs,ConcurrentHashMap<String,DocInterface> docs)
    {
        double alldl = 0.0;
        double N = 0.0;
        for (int j=start;j<end;j++)
        {
            DocVectorAdapter dv = new DocVectorAdapter(dvs.get(j-ori));
            Object doc = docs.get(dv.getDocid());
            if(doc==null)
                continue;
            DocAdapter docinfo = new DocAdapter(doc);
            for(int i=0;i<words.size();i++)
            {
                Integer tf = dv.getTerms().get(words.get(i));
                if(tf==null)
                    continue;
                alldl += docinfo.getText().length();
                N +=1;
                break;
            }
        }

        // 互斥锁
        byte[] lock = new byte[0];
        synchronized(lock)
        {
            this.alldl += alldl;
            this.N += N;
        }
    }

    private static double log(double value, double base)
    {
        return Math.log(value) / Math.log(base);
    }
}
