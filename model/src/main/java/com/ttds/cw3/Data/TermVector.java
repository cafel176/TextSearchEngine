package com.ttds.cw3.Data;

import com.ttds.cw3.Interface.TermVectorInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class TermVector implements TermVectorInterface
{
    private String term = "";
    private HashMap<String,ArrayList<Integer>> postings; // docid, pos

    public TermVector(String term)
    {
        this.term = term;
        this.postings = new HashMap<>();
    }

    public void addPos(String docid, int pos)
    {
        if(postings.containsKey(docid))
            postings.get(docid).add(pos);
        else
        {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(pos);
            postings.put(docid,list);
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

    public void setPostings(HashMap<String, ArrayList<Integer>> postings) {
        this.postings = postings;
    }
}
