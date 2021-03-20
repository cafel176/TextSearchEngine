package com.ttds.cw3.Strategy.Convert;

import com.ttds.cw3.Data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class AnalysisFromJSON extends AnalysisStrategy<JSONAdapter>
{
    @Override
    public ArrayList<Doc> process(JSONAdapter data, String token)
    {
        if(data==null)
            return null;

        ArrayList<Doc> list = new ArrayList<>();

        // 待补充

        return list;
    }

    @Override
    public ArrayList<String> convertTxtList(JSONAdapter data)
    {
        if(data==null)
            return null;

        ArrayList<String> list = new ArrayList<>();

        // 待补充

        return list;
    }
}
