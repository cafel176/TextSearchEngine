package com.ttds.cw3.Strategy.RetrievalModel;

import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.ModelManagerAdapter;
import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;
import java.util.List;

public final class BaseRetrieval extends RetrievalModel
{
    public BaseRetrieval(int thread) {
        super(thread);
    }

    @Override
    protected SearchResult<Double> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DocAdapter docinfo, ModelManagerAdapter m, String other)
    {
        double score = 1.0;

        SearchResult re = new SearchResult(docinfo.getId(),docinfo.getName(),docinfo.getAuthor(),docinfo.getCategory(), score);
        re.setDesc(Double.toString(score));

        return re;
    }

    @Override
    protected void setParams(String param) throws Exception
    {

    }

    @Override
    protected String prepare(int num, List docinfos, ArrayList<String> words,ModelManagerAdapter m)
    {
        return null;
    }
}
