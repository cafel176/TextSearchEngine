package com.ttds.cw3.Factory;

import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Data.JSONAdapter;
import com.ttds.cw3.Strategy.*;
import com.ttds.cw3.Strategy.Read.ReadJSON;
import com.ttds.cw3.Strategy.Read.ReadStrategy;
import com.ttds.cw3.Strategy.Read.ReadTxt;
import com.ttds.cw3.Strategy.Read.ReadXml;

import java.util.ArrayList;

public abstract class ReaderFactory
{
    public static ReadStrategy get(StrategyType type)
    {
        ReadStrategy reader;
        switch (type)
        {
            case txt: reader = getTxtStrategey(); break;
            case xml: reader = getXmlStrategey(); break;
            case json:reader = getJSONStrategey(); break;
            default: reader = null; break;
        }
        return reader;
    }

    private static ReadStrategy<ArrayList<String>> getTxtStrategey()
    {
        return new ReadTxt();
    }

    private static ReadStrategy<ArrayList<Doc>> getXmlStrategey()
    {
        return new ReadXml();
    }

    private static ReadStrategy<JSONAdapter> getJSONStrategey()
    {
        return new ReadJSON();
    }

}
