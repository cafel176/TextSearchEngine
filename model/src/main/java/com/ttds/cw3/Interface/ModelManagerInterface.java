package com.ttds.cw3.Interface;

public interface ModelManagerInterface
{
    boolean setFilePath();
    boolean setSavePath();
    boolean setCategory();
    boolean setStopWordFile();
    boolean loadProperties(String name);

    void initDataAndDoc();
    DataInterface getData();
    PreProcessingInterface getPreProcessing();
    DocInterface getDoc(String id);
}
