package com.ttds.cw3.Strategy.RetrievalModel;

import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.ModelManagerAdapter;
import com.ttds.cw3.Adapter.TermVectorAdapter;
import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;
import java.util.List;

public final class TfidfModel extends RetrievalModel
{
    public TfidfModel(int thread) {
        super(thread);
    }

    @Override
    protected SearchResult<Double> searchDoc(ArrayList<TermVectorAdapter> terms, DocVectorAdapter doc, DocAdapter docinfo, String other)
    {
        double N = Integer.parseInt(other);
        double score = 0.0;

        for(int i=0;i<terms.size();i++)
        {
            Integer tf = doc.getTerms().get(terms.get(i).getTerm());
            if(tf==null)
                continue;
            double df = terms.get(i).getDf();
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
    protected String prepare(int size, int max,int all,  ArrayList<String> words,ModelManagerAdapter m)
    {
        return null;
    }

    private static double log(double value, double base)
    {
        return Math.log(value) / Math.log(base);
    }
}
