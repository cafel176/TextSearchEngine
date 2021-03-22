package com.ttds.cw3.Data;

import com.ttds.cw3.Interface.DocInterface;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "doc")
public class Doc implements DocInterface, Serializable
{
    @Id
    private String id = "";

    private String name = "";
    private String author = "";
    private String category = "";
    private String content = "";

    public Doc(){}

    public Doc(String content,String author, String category)
    {
        setText(content);
        setCategory(category);
        setAuthor(author);
    }

    public Doc(String text)
    {
        setText(text);
    }

    public Doc(JSONAdapter a) {
        this.id = a.getString("id");
        this.name = a.getString("name");
        this.author = a.getString("author");
        this.category = a.getString("category");
        this.content = a.getString("text");
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        if(author!=null && !author.isEmpty())
            this.author = author.trim();
    }

    public String getText()
    {
        return content;
    }

    public void setText(String content)
    {
        if(content!=null && !content.isEmpty())
            this.content = content.trim();
    }
}

