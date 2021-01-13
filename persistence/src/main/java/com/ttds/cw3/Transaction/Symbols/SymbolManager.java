package com.ttds.cw3.Transaction.Symbols;

import com.ttds.cw3.Strategy.RetrievalModel.RetrievalModelType;
import com.ttds.cw3.Strategy.SearchModule.SearchModuleType;
import javafx.util.Pair;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public final class SymbolManager
{
    private HashMap<String,StructSymbolType> otherSymbolMap;
    private HashMap<String,StructSymbolType> singleSymbolMap;
    private HashMap<String, Pair<StructSymbolType,Integer>> doubleSymbolMap;
    private HashMap<String,SearchModuleType> searchSymbolMap;
    private HashMap<String,RetrievalModelType> retrievalSymbolMap;

    public SymbolManager(Properties props)
    {
        loadProperties(props);
    }

    private void init(Properties props)
    {
        String retrieval = props.getProperty("retrieval").trim();
        String search = props.getProperty("search").trim();
        String single = props.getProperty("single").trim();
        String doubles = props.getProperty("double").trim();
        String other = props.getProperty("other").trim();

        Enumeration enu = props.propertyNames();
        final String point = ".";
        final String splitPoint = "\\.";
        while(enu.hasMoreElements())
        {
            String key = (String)enu.nextElement();
            try
            {
                if(key.contains(retrieval+point))
                {
                    String symbol = key.split(splitPoint)[1].trim();
                    RetrievalModelType type = RetrievalModelType.valueOf(symbol);
                    String value = props.getProperty(key).trim();
                    retrievalSymbolMap.put(value,type);
                }
                else if(key.contains(search+point))
                {
                    String symbol = key.split(splitPoint)[1].trim();
                    SearchModuleType type = SearchModuleType.valueOf(symbol);
                    String value = props.getProperty(key).trim();
                    searchSymbolMap.put(value,type);
                }
                else if(key.contains(single+point))
                {
                    String symbol = key.split(splitPoint)[1].trim();
                    StructSymbolType type = StructSymbolType.valueOf(symbol);
                    String value = props.getProperty(key).trim();
                    singleSymbolMap.put(value,type);
                }
                else if(key.contains(other+point))
                {
                    String symbol = key.split(splitPoint)[1].trim();
                    StructSymbolType type = StructSymbolType.valueOf(symbol);
                    String value = props.getProperty(key).trim();
                    otherSymbolMap.put(value,type);
                }
                else if(key.contains(doubles+point))
                {
                    String symbol = key.split(splitPoint)[1].trim();
                    String[] values = props.getProperty(key).trim().split("#");
                    StructSymbolType type = StructSymbolType.valueOf(symbol);
                    doubleSymbolMap.put(values[0],new Pair<>(type,Integer.parseInt(values[1].trim())));
                }
            }
            catch (Exception e)
            {
                System.out.println("符号 "+key+ "解析异常");
            }
        }
    }

    public void loadProperties(Properties props)
    {
        retrievalSymbolMap = new HashMap<>();
        searchSymbolMap = new HashMap<>();
        singleSymbolMap = new HashMap<>();
        doubleSymbolMap = new HashMap<>();
        otherSymbolMap = new HashMap<>();

        init(props);
    }

    public RetrievalModelType TxtToRetrievalTypeAll(String str)
    {
        RetrievalModelType type = retrievalSymbolMap.get(str);
        if(type==null)
        {
            type = RetrievalModelType.base;
        }
        return type;
    }

    public SearchModuleType TxtToSearchTypeAll(String str)
    {
        SearchModuleType type = searchSymbolMap.get(str);
        if(type==null)
        {
            type = SearchModuleType.base;
        }
        return type;
    }

    public StructSymbolType TxtToSymbolAll(String str)
    {
        StructSymbolType type = doubleSymbolMap.get(str).getKey();
        if(type==null)
        {
            type = singleSymbolMap.get(str);
        }
        if(type==null)
        {
            type = StructSymbolType.other;
        }
        return type;
    }

    public StructSymbolType TxtToSymbolSingle(String str)
    {
        StructSymbolType type = singleSymbolMap.get(str);
        if(type==null)
        {
            type = StructSymbolType.other;
        }
        return type;
    }

    public StructSymbolType TxtToSymbolParser(String str)
    {
        StructSymbolType type = otherSymbolMap.get(str);
        if(type==null)
        {
            type = TxtToSymbolDouble(str);
        }
        return type;
    }

    public StructSymbolType TxtToSymbolDouble(String str)
    {
        Pair<StructSymbolType,Integer> p = doubleSymbolMap.get(str);
        StructSymbolType type;
        if(p==null)
        {
            type = StructSymbolType.other;
        }
        else
            type = p.getKey();
        return type;
    }

    public int getLevel(String str)
    {
        Integer level = doubleSymbolMap.get(str).getValue();
        if(level==null)
            return -1;
        else
            return level;
    }
}
