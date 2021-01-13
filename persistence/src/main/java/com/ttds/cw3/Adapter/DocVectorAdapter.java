package com.ttds.cw3.Adapter;

import com.ttds.cw3.Interface.DocVectorInterface;

import java.util.HashMap;

public class DocVectorAdapter
{
    private DocVectorInterface dv;

    public DocVectorAdapter(DocVectorInterface dv) {
        this.dv = dv;
    }

    public DocVectorAdapter(Object dv) {
        this.dv = (DocVectorInterface)dv;
    }

    public void setDv(DocVectorInterface dv) {
        this.dv = dv;
    }

    public String getDocid()
    {
        return dv.getDocid();
    }

    public HashMap<String, Integer> getTerms()
    {
        return dv.getTerms();
    }
}
