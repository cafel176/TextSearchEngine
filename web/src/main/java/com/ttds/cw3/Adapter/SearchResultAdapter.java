package com.ttds.cw3.Adapter;

import com.ttds.cw3.Interface.SearchResultInterface;

public class SearchResultAdapter<T>
{
    private SearchResultInterface<T> result;

    public SearchResultAdapter(SearchResultInterface<T> result) {
        this.result = result;
    }

    public void setResult(SearchResultInterface<T> result) {
        this.result = result;
    }

    public String getDocid()
    {
        return result.getDocid();
    }

    public String getDocName()
    {
        return result.getDocName();
    }

    public T getValue()
    {
        return result.getValue();
    }

    public String getDesc()
    {
        return result.getDesc();
    }
}
