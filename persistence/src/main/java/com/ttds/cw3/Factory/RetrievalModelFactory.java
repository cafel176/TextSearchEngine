package com.ttds.cw3.Factory;

import com.ttds.cw3.Strategy.RetrievalModel.BaseRetrieval;
import com.ttds.cw3.Strategy.RetrievalModel.RetrievalModel;
import com.ttds.cw3.Strategy.RetrievalModel.RetrievalModelType;
import com.ttds.cw3.Strategy.RetrievalModel.TfidfModel;

public abstract class RetrievalModelFactory
{
    public static RetrievalModel get(RetrievalModelType type)
    {
        RetrievalModel module;
        switch (type)
        {
            case tfidf: module = getTfidfModel(); break;
            default: module = getBaseRetrieval(); break;
        }
        return module;
    }

    private static RetrievalModel getBaseRetrieval()
    {
        return new BaseRetrieval();
    }

    private static RetrievalModel getTfidfModel()
    {
        return new TfidfModel();
    }
}
