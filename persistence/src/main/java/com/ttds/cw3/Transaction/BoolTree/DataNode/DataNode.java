package com.ttds.cw3.Transaction.BoolTree.DataNode;

import com.ttds.cw3.Transaction.BoolTree.TreeNode;
import com.ttds.cw3.Transaction.BoolTree.StructNode.StructType;
import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;

public abstract class DataNode<T extends Object> extends TreeNode
{
    protected ArrayList<SearchResult<T>> value;
    protected String text;

    // list至少应该有一个元素
    public DataNode(ArrayList<SearchResult<T>> value)
    {
        this.value = value;
        nodeType = StructType.Data;
    }

    public DataNode(String text)
    {
        this.text = text;
        nodeType = StructType.Data;
    }

    public String getText() {
        return text;
    }

    public ArrayList<SearchResult<T>> getValue()
    {
        return value;
    }

    @Override
    public void setValue(Object value)
    {
        setValue((ArrayList<SearchResult<T>>)value);
    }

    protected void setValue(ArrayList<SearchResult<T>> value)
    {
        this.value = value;
    }

    @Override
    public DataNode<T> getResult()
    {
        return Default();
    }

    public abstract DataNode<T> Default();
    public abstract DataNode<T> Not_Negate();
    public abstract DataNode<T> And_Add(DataNode<T> other);
    public abstract DataNode<T> Or_Subtract(DataNode<T> other);
}
