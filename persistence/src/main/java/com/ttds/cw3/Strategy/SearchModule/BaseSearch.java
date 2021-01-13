package com.ttds.cw3.Strategy.SearchModule;

import com.ttds.cw3.Adapter.DataAdapter;
import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;

public final class BaseSearch extends SearchModule
{
    public BaseSearch(int limit, String pattern) {
        super(limit, pattern);
    }

    @Override
    protected SearchResult<Boolean> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DataAdapter data, DocAdapter docinfo)
    {
        SearchResult<Boolean> re = new SearchResult(doc.getDocid(),true);
        String text = docinfo.getText();
        int pos = data.getTerm(words.get(0)).getPostings().get(doc.getDocid()).get(0);
        re.setDesc(getRelatedStr(pos,text,pattern));

        return re;
    }

    @Override
    protected void setParams(String param) throws Exception
    {

    }
}
