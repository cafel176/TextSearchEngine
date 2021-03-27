package com.ttds.cw3.Data;

import com.ttds.cw3.Adapter.SearchResultAdapter;

public class ResponseData<T extends Object>
{
    private String docid;
    private String docname;
    private String author;
    private String category;
    private String desc;
    private String text;
    private T data;

    public ResponseData(String docid, String docname,String author, String category, String desc, String text, T data) {
        this.docid = docid;
        if(docname==null||docname.isEmpty())
            this.docname = docid;
        else
            this.docname = docname;
        this.data = data;
        this.desc = desc;
        this.author = author;
        this.category = category;
        this.text = text;
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
        author = t.getAuthor();
        category = t.getCategory();
        text = t.getText();
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
