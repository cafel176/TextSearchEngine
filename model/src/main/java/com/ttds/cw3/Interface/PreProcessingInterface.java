package com.ttds.cw3.Interface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface PreProcessingInterface
{
    void doProcessing(String text);
    ConcurrentHashMap<String, ArrayList<Integer>> getTerms();
}
