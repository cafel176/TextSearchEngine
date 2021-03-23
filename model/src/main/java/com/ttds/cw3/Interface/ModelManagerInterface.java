package com.ttds.cw3.Interface;

import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Data.DocVector;
import com.ttds.cw3.Data.TermVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public interface ModelManagerInterface
{
    boolean setFilePath();
    boolean setStopWordFile();
    boolean loadProperties(String name);

    void initDataAndDoc();
    PreProcessingInterface getPreProcessing();

    DocInterface getDoc(String id);
    ArrayList<DocInterface> getDocsByCategory(String category);
    ArrayList<DocInterface> getDocsByAuthor(String author);
    DocVectorInterface getDvByDocid(String docid);
    TermVectorInterface getTermByTerm(String term);
/*
    ArrayList<DocInterface> getDocsById(int pageNo, int pageSize);
    ArrayList<DocInterface> getDocsByCategory(int pageNo, int pageSize);
    ArrayList<DocInterface> getDocsByAuthor(int pageNo, int pageSize);

 */

    ArrayList<TermVectorInterface> getTerms();
    ArrayList<DocVectorInterface> getDvs();

    long getDvsSize();
    long getDocSize();
    long getTermsSize();
}
