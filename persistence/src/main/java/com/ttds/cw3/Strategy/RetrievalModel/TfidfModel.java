package com.ttds.cw3.Strategy.RetrievalModel;

import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.ModelManagerAdapter;
import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;
import java.util.List;

public final class TfidfModel extends RetrievalModel
{
    public TfidfModel(int thread) {
        super(thread);
    }

    @Override
    protected SearchResult<Double> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DocAdapter docinfo, ModelManagerAdapter m, String other)
    {
        double N = m.getDocSize();
        double score = 0.0;

        for(int i=0;i<words.size();i++)
        {
            Integer tf = doc.getTerms().get(words.get(i));
            if(tf==null)
                continue;
            double df = m.getTermByTerm(words.get(i)).getDf();
            double re = (1+log(tf,10)) * log(N/df,10);
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
    protected String prepare(int num, List docs, List docinfos, ArrayList<String> words)
    {
        return null;
    }

    private static double log(double value, double base)
    {
        return Math.log(value) / Math.log(base);
    }
}
