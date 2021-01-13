package com.ttds.cw3.Data;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public final class JSONAdapter
{
    private JSONObject object;

    public JSONAdapter()
    {
        this.object = new JSONObject();
    }

    public JSONAdapter(String object)
    {
        this.object = new JSONObject(object);
    }

    public JSONAdapter(Object object)
    {
        this.object = new JSONObject(object);
    }

    public JSONAdapter(JSONObject object)
    {
        this.object = object;
    }

    public JSONObject getInner() {
        return object;
    }

    public JSONAdapter getObject(String key) {
        return new JSONAdapter(object.getJSONObject(key));
    }

    public String getString(String key)
    {
        return object.getString(key);
    }

    public int getInt(String key)
    {
        return object.getInt(key);
    }

    public double getDouble(String key)
    {
        return object.getDouble(key);
    }

    public Iterator getKeyIterator()
    {
        return object.keys();
    }

    public Object getValue(String key)
    {
        return object.get(key);
    }

    public JSONArrayAdapter getArray(String key)
    {
        return new JSONArrayAdapter(object.getJSONArray(key));
    }

    public void putObject(String key, Object obj)
    {
        object.put(key,obj);
    }
}
