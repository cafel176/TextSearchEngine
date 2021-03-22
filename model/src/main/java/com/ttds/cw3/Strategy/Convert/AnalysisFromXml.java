package com.ttds.cw3.Strategy.Convert;

import com.ttds.cw3.Data.Doc;

import java.util.ArrayList;
import java.util.List;

public final class AnalysisFromXml extends AnalysisStrategy<List<Doc>>
{
    @Override
    public List<Doc> process(List<Doc> data, String token)
    {
        return data;
    }

    @Override
    public List<String> convertTxtList(List<Doc> data)
    {
        if(data==null)
            return null;

        List<String> list = new ArrayList<>();
        for(int i=0;i<data.size();i++)
        {
            list.add(data.get(i).getText());
        }

        return list;
    }
}
