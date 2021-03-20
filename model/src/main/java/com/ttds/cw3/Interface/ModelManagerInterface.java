package com.ttds.cw3.Interface;

import com.ttds.cw3.Data.DocVector;
import com.ttds.cw3.Data.TermVector;

import java.util.List;

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

    long getDocSize();
    long getTermsSize();
    List<TermVectorInterface> getTerms();
    List<DocVectorInterface> getDvs();
    List<DocInterface> getDocs();
}
