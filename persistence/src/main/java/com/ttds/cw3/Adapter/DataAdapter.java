package com.ttds.cw3.Adapter;

import com.ttds.cw3.Interface.DataInterface;
import com.ttds.cw3.Interface.DocVectorInterface;
import com.ttds.cw3.Interface.TermVectorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;

public class DataAdapter
{
    private DataInterface data;

    public DataAdapter(DataInterface data) {
        this.data = data;
    }

    public void setData(DataInterface data) {
        this.data = data;
    }

    public DocVectorAdapter getDv(String key)
    {
        return new DocVectorAdapter(data.getDv(key));
    }

    public TermVectorAdapter getTerm(String key)
    {
        return new TermVectorAdapter(data.getTerm(key));
    }

    public ArrayList<String> getTermKeys()
    {
        return data.getTermKeys();
    }

    public ArrayList<String> getDvsKeys()
    {
        return data.getDvsKeys();
    }

    public Iterator getTermIterator()
    {
        return data.getTermIterator();
    }

    public Iterator getDvsIterator()
    {
        return data.getDvsIterator();
    }

    public int getDvsSize()
    {
        return data.getDvsSize();
    }

    public int getTermsSize()
    {
        return data.getTermsSize();
    }
}
