package com.ttds.cw3.Interface;

import java.util.HashMap;

public interface DocVectorInterface
{
    String getDocid();
    HashMap<String, Integer> getTerms();
}
