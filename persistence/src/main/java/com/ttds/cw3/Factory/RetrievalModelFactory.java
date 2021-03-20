package com.ttds.cw3.Factory;

import com.ttds.cw3.Strategy.RetrievalModel.*;

public abstract class RetrievalModelFactory
{
    public static RetrievalModel get(int thread,RetrievalModelType type)
    {
        RetrievalModel module;
        switch (type)
        {
            case tfidf: module = getTfidfModel(thread); break;
            case bm25: module = getBM25Model(thread); break;
            default: module = getBaseRetrieval(thread); break;
        }
        return module;
    }

    private static RetrievalModel getBaseRetrieval(int thread)
    {
        return new BaseRetrieval(thread);
    }

    private static RetrievalModel getTfidfModel(int thread)
    {
        return new TfidfModel(thread);
    }

    private static BM25Model getBM25Model(int thread)
    {
        return new BM25Model(thread);
    }
}
