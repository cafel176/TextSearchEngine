package com.ttds.cw3.Strategy.RetrievalModel;

import com.ttds.cw3.Adapter.DataAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;

public final class TfidfModel extends RetrievalModel
{
    @Override
    protected SearchResult<Double> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DataAdapter data)
    {
        double N = data.getDvsSize();
        double score = 0.0;

        for(int i=0;i<words.size();i++)
        {
            Integer tf = doc.getTerms().get(words.get(i));
            if(tf==null)
                continue;
            double df = data.getTerm(words.get(i)).getDf();
            double re = (1+log(tf,10)) * log(N/df,10);
            score += re;
        }

        SearchResult re = new SearchResult(doc.getDocid(), score);
        re.setDesc(Double.toString(score));

        return re;
    }

    @Override
    protected void setParams(String param) throws Exception
    {

    }

    private static double log(double value, double base)
    {
        return Math.log(value) / Math.log(base);
    }
}
