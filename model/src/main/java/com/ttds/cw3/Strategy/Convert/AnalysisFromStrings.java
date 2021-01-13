package com.ttds.cw3.Strategy.Convert;

import com.ttds.cw3.Data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class AnalysisFromStrings extends AnalysisStrategy<ArrayList<String>>
{
    @Override
    public ArrayList<Doc> process(ArrayList<String> data, ArrayList<String> category, String token)
    {
        if(data==null)
            return null;

        ArrayList<Doc> list = new ArrayList<>();
        for(int i=0;i<data.size();i++)
        {
            String[] st = data.get(i).trim().split(token);
            Doc d = new Doc(st[1].trim());
            if(category.size()==1 && category.get(0).isEmpty())
            {
                list.add(d);
            }
            else
            {
                String c = st[0].trim();
                if(category.contains(c))
                {
                    d.setCategory(c);
                    list.add(d);
                }
            }

        }
        return list;
    }

    @Override
    public ArrayList<String> convertTxtList(ArrayList<String> data)
    {
        return data;
    }

    @Override
    public Data load(ArrayList<String> t)
    {
        if(t==null)
            return null;

        HashMap<String, DocVector> dvs = new HashMap<>();
        HashMap<String, TermVector> terms = new HashMap<>();

        String symbol = "";
        for(int i=0;i<t.size();i++)
        {
            String txt = t.get(i).trim();
            if(txt.charAt(0)=='$')
                symbol = txt;
            else
            {
                String[] st = txt.split("#v#");
                String key = st[0].trim();
                JSONAdapter a = new JSONAdapter(st[1].trim());
                if(symbol.equals("$dvs"))
                {
                    String docid = a.getString("docid");
                    JSONAdapter o = a.getObject("terms");
                    Iterator it = o.getKeyIterator();
                    HashMap<String, Integer> list = new HashMap<>();
                    while(it.hasNext())
                    {
                        String k = it.next().toString();
                        int value = (int)o.getValue(k);
                        list.put(k, value);
                    }

                    DocVector dv = new DocVector(docid);
                    dv.setTerms(list);
                    dvs.put(key,dv);
                }
                else if(symbol.equals("$terms"))
                {
                    String term = a.getString("term");
                    JSONAdapter o = a.getObject("postings");
                    Iterator it = o.getKeyIterator();
                    HashMap<String, ArrayList<Integer>> list = new HashMap<>();
                    while(it.hasNext())
                    {
                        String k = it.next().toString();
                        JSONArrayAdapter arr = o.getArray(k);
                        ArrayList<Integer> value = new ArrayList<>();
                        for(int j=0;j<arr.length();j++)
                        {
                            value.add(arr.getInt(j));
                        }
                        list.put(k, value);
                    }

                    TermVector tv = new TermVector(term);
                    tv.setPostings(list);
                    terms.put(key,tv);
                }
            }
        }

        Data data = new Data();
        data.setDvs(dvs);
        data.setTerms(terms);
        return data;
    }

    @Override
    public ArrayList<String> save(Data data)
    {
        if(data==null)
            return null;

        ArrayList<String> list = new ArrayList<>();

        HashMap<String, DocVector> dvs = data.getDvs();
        HashMap<String, TermVector> terms = data.getTerms();

        list.add("$dvs");
        Iterator iter = dvs.entrySet().iterator();
        while (iter.hasNext())
        {
            HashMap.Entry entry = (HashMap.Entry) iter.next();
            String key = (String)entry.getKey();
            JSONAdapter a = new JSONAdapter(entry.getValue());
            String dv = a.getInner().toString();
            list.add(key+"#v#"+dv);
        }

        list.add("$terms");
        iter = terms.entrySet().iterator();
        while (iter.hasNext())
        {
            HashMap.Entry entry = (HashMap.Entry) iter.next();
            String key = (String)entry.getKey();
            JSONAdapter a = new JSONAdapter(entry.getValue());
            String term = a.getInner().toString();
            list.add(key+"#v#"+term);
        }

        return list;
    }

    @Override
    public ArrayList<String> saveDocs(ArrayList<Doc> docs)
    {
        if(docs==null)
            return null;

        ArrayList<String> list = new ArrayList<>();
        for(int i=0;i<docs.size();i++)
        {
            list.add((new JSONAdapter(docs.get(i))).getInner().toString());
        }

        return list;
    }

    @Override
    public ArrayList<Doc> loadDocs(ArrayList<String> docs)
    {
        if(docs==null)
            return null;

        ArrayList<Doc> list = new ArrayList<>();
        for(int i=0;i<docs.size();i++)
        {
            list.add(new Doc(new JSONAdapter(docs.get(i))));
        }

        return list;
    }
}
