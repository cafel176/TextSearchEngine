package com.ttds.cw3.Data;

import com.ttds.cw3.Interface.TermVectorInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tvector")
public class TermVector implements TermVectorInterface, Serializable
{
    @Id
    private String term = "";
    private ConcurrentHashMap<String,ArrayList<Integer>> postings; // id, pos

    public TermVector(String term)
    {
        this.term = term;
        this.postings = new ConcurrentHashMap<>();
    }

    public void addPos(String id, int pos)
    {
        if(postings.containsKey(id))
            postings.get(id).add(pos);
        else
        {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(pos);
            postings.put(id,list);
        }
    }

    public String getTerm() {
        return term;
    }

    public int getDf()
    {
        return postings.size();
    }

    public ConcurrentHashMap<String, ArrayList<Integer>> getPostings()
    {
        return postings;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void addPostings(ConcurrentHashMap<String, ArrayList<Integer>> postings)
    {
        ArrayList<ArrayList<Integer>> values = new ArrayList(postings.values());
        ArrayList<String> keys = new ArrayList(postings.keySet());
        for(int i=0;i< keys.size();i++)
        {
            String key = keys.get(i);
            if(this.postings.containsKey(key))
            {
                this.postings.get(key).addAll(values.get(i));
            }
            else
            {
                this.postings.put(key,values.get(i));
            }
        }
    }
}
