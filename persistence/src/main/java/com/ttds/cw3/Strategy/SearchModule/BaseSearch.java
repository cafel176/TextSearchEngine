package com.ttds.cw3.Strategy.SearchModule;

import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.ModelManagerAdapter;
import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;

public final class BaseSearch extends SearchModule
{
    public BaseSearch(int limit, int thread, String pattern) {
        super(limit,thread,pattern);
    }

    @Override
    protected SearchResult<Boolean> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DocAdapter docinfo, ModelManagerAdapter m)
    {
        SearchResult<Boolean> re = new SearchResult(doc.getDocid(),doc.getDocName(),true);
        String text = docinfo.getText();
        ArrayList<Integer> poses = m.getTermByTerm(words.get(0)).getPostings().get(doc.getDocid());
        int pos = 0;
        if(poses!=null)
            pos = poses.get(0);
        re.setDesc(getRelatedStr(pos,text,pattern));

        return re;
    }

    @Override
    protected void setParams(String param) throws Exception
    {

    }
}
