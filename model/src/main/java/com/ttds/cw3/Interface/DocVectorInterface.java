package com.ttds.cw3.Interface;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface DocVectorInterface
{
    String getDocid();
    String getDocName();
    ConcurrentHashMap<String, Integer> getTerms();
}
