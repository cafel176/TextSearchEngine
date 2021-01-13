package com.ttds.cw3.Strategy.Read;

import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Data.JSONAdapter;

import java.util.ArrayList;

public abstract class ReadStrategy<T>
{
    public abstract T read(String file,String encoding);

    public T read(String file)
    {
        String encoding = "UTF-8";
        return read(file, encoding);
    }
}
