package com.ttds.cw3.Interface;

import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Data.DocVector;
import com.ttds.cw3.Data.TermVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface ModelManagerInterface
{
    boolean setFilePath();
    boolean setStopWordFile();
    boolean loadProperties(String name);

    void initDataAndDoc();
    PreProcessingInterface getPreProcessing();

    DocInterface getDoc(String id);
    DocVectorInterface getDvByDocid(String docid);
    TermVectorInterface getTermByTerm(String term);
    List<DocVectorInterface> dvsDBfind(int pageNo, int pageSize,int all);
    ConcurrentHashMap<String,DocInterface> docDBfind(int pageNo, int pageSize, int all);
    long getDocNum();

    void init();
    void test();
}
