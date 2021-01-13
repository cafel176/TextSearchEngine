package com.ttds.cw3.Interface;

import java.util.ArrayList;
import java.util.HashMap;

public interface PreProcessingInterface
{
    void doProcessing(String text);
    HashMap<String, ArrayList<Integer>> getTerms();
}
