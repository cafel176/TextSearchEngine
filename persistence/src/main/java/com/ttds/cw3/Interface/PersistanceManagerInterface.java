package com.ttds.cw3.Interface;

import com.ttds.cw3.Data.SearchResult;
import javafx.util.Pair;

import java.util.ArrayList;

public interface PersistanceManagerInterface
{
    boolean loadProperties(String name);
    Pair<OtherParamsInterface,ArrayList<SearchResultInterface<Double>>> search(String str, int start, int end) throws Exception;
    Pair<OtherParamsInterface,ArrayList<SearchResultInterface<Double>>> searchByRetrievalModel(String str, int start, int end) throws Exception;
    Pair<OtherParamsInterface,ArrayList<SearchResultInterface<Boolean>>> searchBySearchModule(String str, int start, int end) throws Exception;

    void init();
    void test();
}
