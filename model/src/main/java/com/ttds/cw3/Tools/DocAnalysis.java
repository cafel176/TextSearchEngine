package com.ttds.cw3.Tools;

import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Strategy.Convert.AnalysisStrategy;

import java.util.ArrayList;
import java.util.List;

public final class DocAnalysis
{
    public List<Doc> process(AnalysisStrategy converter, Object data, String token)
    {
        if(converter==null)
        {
            System.out.println("Converter加载出错！");
            return null;
        }
        return converter.process(data,token);
    }

    public List<String> txtsfrom(AnalysisStrategy converter, Object data)
    {
        if(converter==null)
        {
            System.out.println("Converter加载出错！");
            return null;
        }
        return converter.convertTxtList(data);
    }
}
