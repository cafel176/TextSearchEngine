package com.ttds.cw3.Factory;

import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Data.JSONAdapter;
import com.ttds.cw3.Strategy.*;
import com.ttds.cw3.Strategy.Convert.AnalysisFromJSON;
import com.ttds.cw3.Strategy.Convert.AnalysisFromStrings;
import com.ttds.cw3.Strategy.Convert.AnalysisFromXml;
import com.ttds.cw3.Strategy.Convert.AnalysisStrategy;

import java.util.ArrayList;
import java.util.List;

public abstract class AnalysisFactory
{
    public static AnalysisStrategy get(StrategyType type)
    {
        AnalysisStrategy converter;
        switch (type)
        {
            case txt: converter = getTxtStrategey(); break;
            case xml: converter = getXmlStrategey(); break;
            case json:converter = getJSONStrategey(); break;
            default: converter = null; break;
        }
        return converter;
    }

    private static AnalysisStrategy<List<String>> getTxtStrategey()
    {
        return new AnalysisFromStrings();
    }

    private static AnalysisStrategy<JSONAdapter> getJSONStrategey()
    {
        return new AnalysisFromJSON();
    }

    private static AnalysisStrategy<ArrayList<Doc>> getXmlStrategey()
    {
        return new AnalysisFromXml();
    }
}
