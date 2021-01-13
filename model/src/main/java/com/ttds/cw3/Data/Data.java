package com.ttds.cw3.Data;

import com.ttds.cw3.Interface.DataInterface;
import com.ttds.cw3.Interface.DocVectorInterface;
import com.ttds.cw3.Interface.TermVectorInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Data implements DataInterface
{
    private HashMap<String, DocVector> dvs;
    private HashMap<String, TermVector> terms;

    public Data()
    {
        dvs = new HashMap<>();
        terms = new HashMap<>();
    }

    public HashMap<String, DocVector> getDvs() {
        return dvs;
    }

    public void setDvs(HashMap<String, DocVector> dvs) {
        this.dvs = dvs;
    }

    public HashMap<String, TermVector> getTerms() {
        return terms;
    }

    public DocVectorInterface getDv(String key)
    {
        return dvs.get(key);
    }

    public TermVectorInterface getTerm(String key)
    {
        return terms.get(key);
    }

    public ArrayList<String> getTermKeys()
    {
        return new ArrayList<String>(terms.keySet());
    }

    public ArrayList<String> getDvsKeys()
    {
        return new ArrayList<String>(dvs.keySet());
    }

    public Iterator getTermIterator(){
        return terms.entrySet().iterator();
    }

    public Iterator getDvsIterator(){
        return dvs.entrySet().iterator();
    }

    public int getDvsSize(){
        return dvs.size();
    }

    public int getTermsSize(){
        return terms.size();
    }

    public void setTerms(HashMap<String, TermVector> terms) {
        this.terms = terms;
    }

    public void addDocVector(String docid, String term)
    {
        if(dvs.containsKey(docid))
            dvs.get(docid).addTerm(term);
        else
        {
            DocVector dv = new DocVector(docid);
            dv.addTerm(term);
            dvs.put(docid, dv);
        }
    }

    public void addTermVector(String term, String docid, int pos)
    {
        if(terms.containsKey(term))
            terms.get(term).addPos(docid,pos);
        else
        {
            TermVector t = new TermVector(term);
            t.addPos(docid, pos);
            terms.put(term, t);
        }
    }

    public void clear()
    {
        dvs.clear();
        terms.clear();
    }
}
