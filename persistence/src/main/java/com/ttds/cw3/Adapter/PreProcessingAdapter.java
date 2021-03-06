package com.ttds.cw3.Adapter;

import com.ttds.cw3.Interface.PreProcessingInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class PreProcessingAdapter
{
    private PreProcessingInterface pp;

    public PreProcessingAdapter(PreProcessingInterface pp) {
        this.pp = pp;
    }

    public void setPp(PreProcessingInterface pp) {
        this.pp = pp;
    }

    public void doProcessing(String text)
    {
        pp.doProcessing(text);
    }

    public ConcurrentHashMap<String, ArrayList<Integer>> getTerms()
    {
        return pp.getTerms();
    }
}
