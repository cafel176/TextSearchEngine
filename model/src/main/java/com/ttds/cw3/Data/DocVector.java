package com.ttds.cw3.Data;

import com.ttds.cw3.Interface.DocVectorInterface;

import java.util.HashMap;

public class DocVector implements DocVectorInterface
{
    private String docid;
    private HashMap<String,Integer> terms;

    public DocVector(String docid)
    {
        this.docid = docid;
        this.terms = new HashMap<>();
    }

    public void addTerm(String term)
    {
        if(terms.containsKey(term))
        {
            int num = terms.get(term);
            terms.replace(term,num+1);
        }
        else
        {
            terms.put(term,1);
        }
    }

    public String getDocid() {
        return docid;
    }

    public HashMap<String, Integer> getTerms() {
        return terms;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public void setTerms(HashMap<String, Integer> terms) {
        this.terms = terms;
    }
}
