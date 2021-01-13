package com.ttds.cw3.Strategy.Write;

import java.util.ArrayList;

public abstract class WriteStrategy<T>
{
    public abstract boolean write(String file, T data, String encoding);

    public boolean write(String file, T data)
    {
        String encoding = "UTF-8";
        return write(file, data, encoding);
    }
}
