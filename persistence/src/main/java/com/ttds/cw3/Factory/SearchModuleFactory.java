package com.ttds.cw3.Factory;

import com.ttds.cw3.Strategy.SearchModule.*;

public abstract class SearchModuleFactory
{
    public static SearchModule get(SearchModuleType type,int limit, String pattern)
    {
        SearchModule module;
        switch (type)
        {
            case phrase: module = getPhraseModule(limit,pattern); break;
            case proximity: module = getProximityModule(limit,pattern); break;
            default: module = getBaseModule(limit,pattern); break;
        }
        return module;
    }

    private static SearchModule getBaseModule(int limit, String pattern)
    {
        return new BaseSearch(limit,pattern);
    }

    private static SearchModule getPhraseModule(int limit, String pattern)
    {
        return new PhraseSearch(limit,pattern);
    }

    private static SearchModule getProximityModule(int limit, String pattern)
    {
        return new ProximitySearch(limit,pattern);
    }
}
