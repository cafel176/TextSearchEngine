package com.ttds.cw3.Data;

import com.ttds.cw3.Interface.SearchResultInterface;

public class SearchResult<T extends Object> implements SearchResultInterface
{
    private String docid;
    private String docName;
    private String desc;
    private T value;

    public SearchResult(String docid, String docName, T value) {
        this.docid = docid;
        this.docName = docName;
        this.value = value;
    }

    public SearchResult(SearchResult other) {
        this.docid = other.getDocid();
        this.docName = other.getDocName();
        this.value = (T)other.getValue();
        this.desc = other.getDesc();
    }

    public String getDocid() {
        return docid;
    }

    public String getDocName() {
        return docName;
    }

    public T getValue() {
        return value;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
