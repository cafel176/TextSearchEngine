package com.ttds.cw3.Data;

import org.json.JSONArray;

public final class JSONArrayAdapter
{
    private JSONArray array;

    public JSONArrayAdapter(JSONArray array)
    {
        this.array = array;
    }

    public int length()
    {
        return array.length();
    }

    public JSONAdapter get(int index)
    {
        return new JSONAdapter(array.getJSONObject(index));
    }

    public String getString(int key)
    {
        return array.getString(key);
    }

    public int getInt(int key)
    {
        return array.getInt(key);
    }

    public double getDouble(int key)
    {
        return array.getDouble(key);
    }
}
