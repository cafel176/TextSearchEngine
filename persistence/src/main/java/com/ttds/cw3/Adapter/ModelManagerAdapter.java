package com.ttds.cw3.Adapter;

import com.ttds.cw3.Data.DocVector;
import com.ttds.cw3.Data.TermVector;
import com.ttds.cw3.Interface.DocInterface;
import com.ttds.cw3.Interface.DocVectorInterface;
import com.ttds.cw3.Interface.ModelManagerInterface;
import com.ttds.cw3.Interface.TermVectorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public PreProcessingAdapter getPreProcessing()
    {
        return new PreProcessingAdapter(manager.getPreProcessing());
    }

    public DocAdapter getDoc(String id)
    {
        DocInterface i = manager.getDoc(id);
        if(i==null)
            return null;
        else
            return new DocAdapter(manager.getDoc(id));
    }

    public DocVectorAdapter getDvByDocid(String docid)
    {
        DocVectorInterface i = manager.getDvByDocid(docid);
        if(i==null)
            return null;
        else
            return new DocVectorAdapter(i);
    }

    public TermVectorAdapter getTermByTerm(String term)
    {
        TermVectorInterface i = manager.getTermByTerm(term);
        if(i==null)
            return null;
        else
            return new TermVectorAdapter(i);
    }

    public int getDocSize()
    {
        return (int) manager.getDocSize();
    }

    public int getTermsSize()
    {
        return (int) manager.getTermsSize();
    }

    public List getTerms()
    {
        return manager.getTerms();
    }

    public List getDvs()
    {
        return manager.getDvs();
    }

    public List getDocs()
    {
        return manager.getDocs();
    }
}
