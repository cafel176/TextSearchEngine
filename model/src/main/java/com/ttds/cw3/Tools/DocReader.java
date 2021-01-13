package com.ttds.cw3.Tools;

import com.ttds.cw3.Strategy.Read.ReadStrategy;

import java.util.ArrayList;

public final class DocReader
{
    private String filePath;

    public DocReader(String filePath)
    {
        this.filePath = filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String getFullPath(String fileName)
    {
        return filePath + fileName;
    }

    public Object get(String fileName, ReadStrategy reader)
    {
        if(reader==null)
        {
            System.out.println("Reader加载出错！");
            return null;
        }
        return reader.read(getFullPath(fileName));
    }
}
