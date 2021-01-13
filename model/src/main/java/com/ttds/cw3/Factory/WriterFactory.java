package com.ttds.cw3.Factory;

import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Data.JSONAdapter;
import com.ttds.cw3.Strategy.*;
import com.ttds.cw3.Strategy.Write.WriteFromDocList;
import com.ttds.cw3.Strategy.Write.WriteFromStringList;
import com.ttds.cw3.Strategy.Write.WriteJSON;
import com.ttds.cw3.Strategy.Write.WriteStrategy;

import java.util.ArrayList;

public abstract class WriterFactory
{
    public static WriteStrategy get(StrategyType type)
    {
        WriteStrategy writer;
        switch (type)
        {
            case txt: writer = getTxtListStrategey(); break;
            case xml: writer = getXmlStrategey(); break;
            case json:writer = getJSONStrategey(); break;
            default: writer = null; break;
        }
        return writer;
    }

    private static WriteStrategy<ArrayList<String>> getTxtListStrategey()
    {
        return new WriteFromStringList();
    }

    private static WriteStrategy<ArrayList<Doc>> getXmlStrategey()
    {
        return new WriteFromDocList();
    }

    private static WriteStrategy<JSONAdapter> getJSONStrategey()
    {
        return new WriteJSON();
    }
}
