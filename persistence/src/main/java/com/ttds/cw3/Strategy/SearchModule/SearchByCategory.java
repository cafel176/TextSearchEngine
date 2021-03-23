package com.ttds.cw3.Strategy.SearchModule;

import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.ModelManagerAdapter;
import com.ttds.cw3.Adapter.PreProcessingAdapter;
import com.ttds.cw3.Data.SearchResult;
import com.ttds.cw3.Interface.DocInterface;

import java.util.ArrayList;
import java.util.List;

public final class SearchByCategory extends SearchModule
{
    private ArrayList<String> docs = new ArrayList<>();

    public SearchByCategory(int limit, int thread, String pattern)
    {
        super(limit,thread,pattern);
    }

    @Override
    protected void inThread(int size, int start, int end, ArrayList<String> words, List dvs, SearchResult<Boolean>[] results, ModelManagerAdapter m)
    {
        synchronized(docs)
        {
            if(docs.isEmpty())
            {
                for (String word : words)
                {
                    ArrayList<DocInterface> list = m.getDocsByCategory(word);
                    for(int i=0;i<list.size();i++)
                    {
                        docs.add(list.get(i).getId());
                    }
                }
            }
        }

        for(int j=start;j<end;j++)
        {
            DocVectorAdapter dv = new DocVectorAdapter(dvs.get(j));
            if(dv==null)
                continue;

            // =============== 筛选步骤 ===============
            if(docs.contains(dv.getDocid()))
                results[j] = searchDoc(words,dv,m.getDoc(dv.getDocid()),m);
            else
                results[j] = new SearchResult(dv.getDocid(),dv.getDocName(), false);
        }
    }

    @Override
    protected SearchResult<Boolean> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DocAdapter docinfo, ModelManagerAdapter m)
    {
        SearchResult<Boolean> re = new SearchResult(doc.getDocid(),doc.getDocName(),true);
        String text = docinfo.getText();
        re.setDesc(getRelatedStr(0,text,pattern));

        return re;
    }

    @Override
    protected void setParams(String param) throws Exception
    {

    }
}
