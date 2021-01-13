package com.ttds.cw3.Interface;

import java.util.ArrayList;
import java.util.Iterator;

public interface DataInterface
{
    DocVectorInterface getDv(String key);
    TermVectorInterface getTerm(String key);
    ArrayList<String> getTermKeys();
    ArrayList<String> getDvsKeys();
    Iterator getTermIterator();
    Iterator getDvsIterator();
    int getDvsSize();
    int getTermsSize();
}
