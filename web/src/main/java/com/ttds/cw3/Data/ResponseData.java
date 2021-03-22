package com.ttds.cw3.Data;

import com.ttds.cw3.Adapter.SearchResultAdapter;

public class ResponseData<T extends Object>
{
    private String docid;
    private String docname;
    private String desc;
    private T data;

    public ResponseData(String docid, String docname, T data, String desc) {
        this.docid = docid;
        if(docname==null||docname.isEmpty())
            this.docname = docid;
        else
            this.docname = docname;
        this.data = data;
        this.desc = desc;
    }

    public ResponseData(SearchResultAdapter<T> t)
    {
        docid = t.getDocid();
        String docname = t.getDocName();
        if(docname==null||docname.isEmpty())
            this.docname = docid;
        else
            this.docname = docname;
        data = t.getValue();
        desc = t.getDesc();
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getDocid() {
        return docid;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
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
