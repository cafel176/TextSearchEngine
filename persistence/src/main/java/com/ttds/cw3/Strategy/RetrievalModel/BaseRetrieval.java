package com.ttds.cw3.Strategy.RetrievalModel;

import com.ttds.cw3.Adapter.DataAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;

public final class BaseRetrieval extends RetrievalModel
{
    @Override
    protected SearchResult<Double> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DataAdapter data)
    {
        double score = 0.0;

        for(int i=0;i<words.size();i++)
        {
            Integer tf = doc.getTerms().get(words.get(i));
            if(tf==null)
                continue;
            score += 1.0;
        }

        SearchResult re = new SearchResult(doc.getDocid(), score);
        re.setDesc(Double.toString(score));
        return re;
    }

    @Override
    protected void setParams(String param) throws Exception
    {

    }
}
