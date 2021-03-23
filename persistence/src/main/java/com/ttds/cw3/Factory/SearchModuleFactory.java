package com.ttds.cw3.Factory;

import com.ttds.cw3.Strategy.SearchModule.*;

public abstract class SearchModuleFactory
{
    public static SearchModule get(SearchModuleType type,int limit,int thread, String pattern)
    {
        SearchModule module;
        switch (type)
        {
            case phrase: module = getPhraseModule(limit,thread,pattern); break;
            case proximity: module = getProximityModule(limit,thread,pattern); break;
            case category: module = getCategoryModule(limit,thread,pattern); break;
            case author: module = getAuthorModule(limit,thread,pattern); break;
            default: module = getBaseModule(limit,thread,pattern); break;
        }
        return module;
    }

    private static SearchModule getBaseModule(int limit,int thread, String pattern)
    {
        return new BaseSearch(limit,thread,pattern);
    }

    private static SearchModule getPhraseModule(int limit,int thread, String pattern)
    {
        return new PhraseSearch(limit,thread,pattern);
    }

    private static SearchModule getProximityModule(int limit, int thread,String pattern)
    {
        return new ProximitySearch(limit,thread,pattern);
    }

    private static SearchModule getCategoryModule(int limit,int thread, String pattern)
    {
        return new SearchByCategory(limit,thread,pattern);
    }

    private static SearchModule getAuthorModule(int limit,int thread, String pattern)
    {
        return new SearchByAuthor(limit,thread,pattern);
    }
}
