package com.ttds.cw3.Interface;

public interface SearchResultInterface<T>
{
    String getDocid();
    String getDocName();
    T getValue();
    String getDesc();
    String getAuthor();
    String getCategory();
    String getText();
}
