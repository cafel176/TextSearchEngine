package com.ttds.cw3.Strategy.RetrievalModel;

import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.ModelManagerAdapter;
import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;
import java.util.List;
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
    protected SearchResult<Double> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DocAdapter docinfo, ModelManagerAdapter m,String other)
    {
        double N = m.getDocSize();
        double score = 0.0;
        double k = 1.2;
        double b = 0.75;
        double avgdl = this.alldl/this.N;

        for(int i=0;i<words.size();i++)
        {
            Integer tf = doc.getTerms().get(words.get(i));
            if(tf==null)
                continue;
            double dl = docinfo.getText().length();
            double l = dl/avgdl;
            double df = m.getTermByTerm(words.get(i)).getDf();
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
    protected String prepare(int num, List docs,List docinfos, ArrayList<String> words)
    {
        if(thread<=0)
            process(0, num, docs,docinfos, words);
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
                        process(start, end, docs,docinfos, words);
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

        return null;
    }

    private void process(int start,int end,List docs,List docinfos, ArrayList<String> words)
    {
        double alldl = 0.0;
        double N = 0.0;
        for (int j=start;j<end;j++)
        {
            DocVectorAdapter doc = new DocVectorAdapter(docs.get(j));
            DocAdapter docinfo = new DocAdapter(docinfos.get(j));
            for(int i=0;i<words.size();i++)
            {
                Integer tf = doc.getTerms().get(words.get(i));
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
