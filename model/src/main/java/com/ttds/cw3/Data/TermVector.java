package com.ttds.cw3.Data;

import com.ttds.cw3.Interface.TermVectorInterface;

import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tvector")
public class TermVector implements TermVectorInterface
{
    @Id
    private String term = "";
    private HashMap<String,ArrayList<Integer>> postings; // id, pos

    public TermVector(String term)
    {
        this.term = term;
        this.postings = new HashMap<>();
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

    public HashMap<String, ArrayList<Integer>> getPostings()
    {
        return postings;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setPostings(HashMap<String, ArrayList<Integer>> postings)
    {
        HashMap<String, ArrayList<Integer>> map = new HashMap<>();
        map.putAll(postings);
        this.postings = map;
    }
}
