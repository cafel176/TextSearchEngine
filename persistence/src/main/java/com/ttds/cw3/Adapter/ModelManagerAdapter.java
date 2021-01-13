package com.ttds.cw3.Adapter;

import com.ttds.cw3.Interface.DocInterface;
import com.ttds.cw3.Interface.ModelManagerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelManagerAdapter
{
    private ModelManagerInterface manager;

    @Autowired
    public ModelManagerAdapter(ModelManagerInterface manager) {
        this.manager = manager;
    }

    public boolean setFilePath()
    {
        return manager.setFilePath();
    }

    public boolean setSavePath()
    {
        return manager.setSavePath();
    }

    public boolean setCategory()
    {
        return manager.setCategory();
    }

    public boolean setStopWordFile()
    {
        return manager.setStopWordFile();
    }

    public boolean loadProperties(String name)
    {
        return manager.loadProperties(name);
    }

    public void initDataAndDoc()
    {
        manager.initDataAndDoc();
    }

    public DataAdapter getData()
    {
        return new DataAdapter(manager.getData());
    }

    public PreProcessingAdapter getPreProcessing()
    {
        return new PreProcessingAdapter(manager.getPreProcessing());
    }

    public DocAdapter getDoc(String docid)
    {
        return new DocAdapter(manager.getDoc(docid));
    }
}
