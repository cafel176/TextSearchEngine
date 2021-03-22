package com.ttds.cw3.Strategy.Convert;

import com.ttds.cw3.Data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class AnalysisFromStrings extends AnalysisStrategy<List<String>>
{
    @Override
    public List<Doc> process(List<String> data, String token)
    {
        if(data==null)
            return null;

        ArrayList<Doc> list = new ArrayList<>();
        for(int i=0;i<data.size();i++)
        {
            String temp = data.get(i).trim();
            String[] st = temp.split(token);
            String content = st[0],author = null,category = null;
            if(st.length>=2)
                author = st[1];
            if(st.length>=3)
                category = st[2];
            list.add(new Doc(content,author,category));
        }
        return list;
    }

    @Override
    public List<String> convertTxtList(List<String> data)
    {
        return data;
    }
}
