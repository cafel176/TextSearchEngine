package com.ttds.cw3.Data;

import com.ttds.cw3.Interface.DocInterface;

public class Doc implements DocInterface
{
    private String id = "";
    private String category = "";
    private String text = "";

    public Doc() { }

    public Doc(String text)
    {
        setText(text);
    }

    public Doc(JSONAdapter a) {
        this.id = a.getString("id");
        this.category = a.getString("category");
        this.text = a.getString("text");
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        if(id!=null && !id.isEmpty())
            this.id = id;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        if(category!=null && !category.isEmpty())
            this.category = category.trim();
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        if(text!=null && !text.isEmpty())
            this.text = text.trim();
    }
}

