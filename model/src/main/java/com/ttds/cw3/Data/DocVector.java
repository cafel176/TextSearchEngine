package com.ttds.cw3.Data;

import com.ttds.cw3.Interface.DocVectorInterface;

import java.util.HashMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dvector")
public class DocVector implements DocVectorInterface
{
    @Id
    private String docid = "";
    private String docName = "";
    private HashMap<String,Integer> terms;

    public DocVector(String docid, String docName)
    {
        this.docid = docid;
        this.docName = docName;
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

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public HashMap<String, Integer> getTerms() {
        return terms;
    }

    public void setTerms(HashMap<String, Integer> terms)
    {
        HashMap<String, Integer> map = new HashMap<>();
        map.putAll(terms);
        this.terms = map;
    }
}
