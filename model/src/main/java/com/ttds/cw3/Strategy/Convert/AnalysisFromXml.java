package com.ttds.cw3.Strategy.Convert;

import com.ttds.cw3.Data.Doc;

import java.util.ArrayList;

public final class AnalysisFromXml extends AnalysisStrategy<ArrayList<Doc>>
{
    @Override
    public ArrayList<Doc> process(ArrayList<Doc> data, String token)
    {
        return data;
    }

    @Override
    public ArrayList<String> convertTxtList(ArrayList<Doc> data)
    {
        if(data==null)
            return null;

        ArrayList<String> list = new ArrayList<>();
        for(int i=0;i<data.size();i++)
        {
            list.add(data.get(i).getText());
        }

        return list;
    }
}
