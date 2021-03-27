package com.ttds.cw3.Data;

import com.ttds.cw3.Interface.SearchResultInterface;

public class SearchResult<T extends Object> implements SearchResultInterface
{
    private String docid;
    private String docname;
    private String author;
    private String category;
    private String desc;
    private String text;
    private T value;

    public SearchResult(String docid, String docname,String author,String category, T value) {
        this.docid = docid;
        this.docname = docname;
        this.value = value;
        this.author = author;
        this.category = category;
    }

    public SearchResult(SearchResult other) {
        this.docid = other.getDocid();
        this.docname = other.getDocName();
        this.value = (T)other.getValue();
        this.desc = other.getDesc();
        this.author = other.author;
        this.category = other.category;
        this.text = other.text;
    }

    public String getDocid() {
        return docid;
    }

    public String getDocName() {
        return docname;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
