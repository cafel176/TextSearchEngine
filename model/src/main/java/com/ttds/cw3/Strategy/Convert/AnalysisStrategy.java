package com.ttds.cw3.Strategy.Convert;

import com.ttds.cw3.Data.Doc;

import java.util.ArrayList;
import java.util.List;

public abstract class AnalysisStrategy<T>
{
    public abstract List<Doc> process(T t, String token);

    public abstract List<String> convertTxtList(T t);
}
