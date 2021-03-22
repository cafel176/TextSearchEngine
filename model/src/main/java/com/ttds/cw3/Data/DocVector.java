package com.ttds.cw3.Data;

import com.ttds.cw3.Interface.DocVectorInterface;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.concurrent.ConcurrentHashMap;

@Document(collection = "dvector")
public class DocVector implements DocVectorInterface
{
    @Id
    private String docid = "";
    private String docname = "";
    private ConcurrentHashMap<String,Integer> terms;

    public DocVector(String docid, String docname)
    {
        this.docid = docid;
        this.docname = docname;
        this.terms = new ConcurrentHashMap<>();
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

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getDocName() {
        return docname;
    }

    public void setDocName(String docname) {
        this.docname = docname;
    }

    public ConcurrentHashMap<String, Integer> getTerms() {
        return terms;
    }

    public void setTerms(ConcurrentHashMap<String, Integer> terms)
    {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.putAll(terms);
        this.terms = map;
    }
}
