package com.ttds.cw3.Strategy.Convert;

import com.ttds.cw3.Data.Data;
import com.ttds.cw3.Data.Doc;

import java.util.ArrayList;

public abstract class AnalysisStrategy<T>
{
    public abstract ArrayList<Doc> process(T t, ArrayList<String> category, String token);

    public abstract ArrayList<String> convertTxtList(T t);

    public abstract Data load(T t);

    public abstract T save(Data data);

    public abstract T saveDocs(ArrayList<Doc> docs);

    public abstract ArrayList<Doc> loadDocs(T t);
}
