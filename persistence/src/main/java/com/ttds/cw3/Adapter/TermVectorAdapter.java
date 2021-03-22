package com.ttds.cw3.Adapter;

import com.ttds.cw3.Interface.TermVectorInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TermVectorAdapter
{
    private TermVectorInterface tv;

    public TermVectorAdapter(TermVectorInterface tv) {
        this.tv = tv;
    }

    public TermVectorAdapter(Object tv) {
        this.tv = (TermVectorInterface)tv;
    }

    public void setTv(TermVectorInterface tv) {
        this.tv = tv;
    }

    public String getTerm()
    {
        return tv.getTerm();
    }

    public int getDf()
    {
        return tv.getDf();
    }

    public ConcurrentHashMap<String, ArrayList<Integer>> getPostings()
    {
        return tv.getPostings();
    }
}
