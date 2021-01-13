package com.ttds.cw3.Strategy.Convert;

import com.ttds.cw3.Data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class AnalysisFromJSON extends AnalysisStrategy<JSONAdapter>
{
    @Override
    public ArrayList<Doc> process(JSONAdapter data,ArrayList<String> category, String token)
    {
        if(data==null)
            return null;

        ArrayList<Doc> list = new ArrayList<>();

        // 待补充

        return list;
    }

    @Override
    public ArrayList<String> convertTxtList(JSONAdapter data)
    {
        if(data==null)
            return null;

        ArrayList<String> list = new ArrayList<>();

        // 待补充

        return list;
    }

    @Override
    public ArrayList<Doc> loadDocs(JSONAdapter data)
    {
        if(data==null)
            return null;

        ArrayList<Doc> list = new ArrayList<>();

        // 待补充

        return list;
    }

    @Override
    public JSONAdapter saveDocs(ArrayList<Doc> docs)
    {
        if(docs==null)
            return null;

        return new JSONAdapter(docs);
    }

    @Override
    public Data load(JSONAdapter obj)
    {
        if(obj==null)
            return null;

        JSONAdapter _dvs = obj.getObject("dvs");
        JSONAdapter _terms = obj.getObject("terms");
        HashMap<String, DocVector> dvs = new HashMap<>();
        HashMap<String, TermVector> terms = new HashMap<>();

        Iterator it = _dvs.getKeyIterator();
        while(it.hasNext())
        {
            String key = it.next().toString();
            DocVector value = (DocVector)_dvs.getValue(key);
            dvs.put(key, value);
        }

        it = _terms.getKeyIterator();
        while(it.hasNext())
        {
            String key = it.next().toString();
            TermVector value = (TermVector)_terms.getValue(key);
            terms.put(key, value);
        }

        Data data = new Data();
        data.setDvs(dvs);
        data.setTerms(terms);

        return data;
    }

    @Override
    public JSONAdapter save(Data data)
    {
        if(data==null)
            return null;

        JSONAdapter _dvs = new JSONAdapter(data.getDvs());
        JSONAdapter _terms = new JSONAdapter(data.getTerms());

        JSONAdapter obj = new JSONAdapter();
        obj.putObject("dvs",_dvs.getInner());
        obj.putObject("terms",_terms.getInner());

        return obj;
    }
}
