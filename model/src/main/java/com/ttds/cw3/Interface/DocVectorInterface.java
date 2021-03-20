package com.ttds.cw3.Interface;

import java.util.HashMap;

public interface DocVectorInterface
{
    String getDocid();
    String getDocName();
    HashMap<String, Integer> getTerms();
}
