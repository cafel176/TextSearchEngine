package com.ttds.cw3.Adapter;

import com.ttds.cw3.Interface.DocInterface;

public class DocAdapter
{
    private DocInterface doc;

    public DocAdapter(DocInterface doc) {
        this.doc = doc;
    }

    public void setDoc(DocInterface doc) {
        this.doc = doc;
    }

    public String getId()
    {
        return doc.getId();
    }

    public String getCategory()
    {
        return getCategory();
    }

    public String getText()
    {
        return doc.getText();
    }
}
