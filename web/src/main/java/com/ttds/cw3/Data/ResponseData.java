package com.ttds.cw3.Data;

import com.ttds.cw3.Adapter.SearchResultAdapter;

public class ResponseData<T extends Object>
{
    private String docid;
    private String desc;
    private T data;

    public ResponseData(String docid, T data, String desc) {
        this.docid = docid;
        this.data = data;
        this.desc = desc;
    }

    public ResponseData(SearchResultAdapter<T> t)
    {
        docid = t.getDocid();
        data = t.getValue();
        desc = t.getDesc();
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getDocid() {
        return docid;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
