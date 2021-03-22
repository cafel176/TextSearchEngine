package com.ttds.cw3.Interface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface TermVectorInterface
{
    String getTerm();
    int getDf();
    ConcurrentHashMap<String, ArrayList<Integer>> getPostings();
}
