package com.ttds.cw3.Tools;

import com.ttds.cw3.Strategy.Write.WriteStrategy;

import java.util.ArrayList;

public final class DocWriter
{
    private String filePath;

    public DocWriter(String filePath) {
        this.filePath = filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String getFullPath(String fileName)
    {
        return filePath + fileName;
    }

    public boolean write(String fileName, WriteStrategy writer, Object data)
    {
        if(writer==null)
        {
            System.out.println("Writer加载出错！");
            return false;
        }
        return writer.write(getFullPath(fileName), data);
    }

}
