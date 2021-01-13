package com.ttds.cw3.Factory;

import com.ttds.cw3.Transaction.BoolTree.*;
import com.ttds.cw3.Transaction.BoolTree.DataNode.BoolNode;
import com.ttds.cw3.Transaction.BoolTree.DataNode.DataNode;
import com.ttds.cw3.Transaction.BoolTree.DataNode.DataType;
import com.ttds.cw3.Transaction.BoolTree.DataNode.DoubleNode;
import com.ttds.cw3.Transaction.BoolTree.StructNode.*;
import com.ttds.cw3.Data.SearchResult;
import com.ttds.cw3.Transaction.Symbols.StructSymbolType;

import java.util.ArrayList;

public abstract class TreeNodeFactory
{
    public static DataNode getDataNode(DataType type, String text)
    {
        DataNode node;
        switch (type)
        {
            case Double: node = getDoubleNode(text); break;
            case Boolean: node = getBoolNode(text); break;
            default: node = null; break;
        }
        return node;
    }

    public static StructType SymbolToType(StructSymbolType s)
    {
        StructType type;
        switch (s)
        {
            case and: type = StructType.And_Add; break;
            case or: type = StructType.Or_Subtract; break;
            case not: type = StructType.Not_Negate; break;
            case add: type = StructType.And_Add; break;
            case subtract: type = StructType.Or_Subtract; break;
            case negate: type = StructType.Not_Negate; break;
            default: type = StructType.Other; break;
        }
        return type;
    }

    public static StructNode getStructNode(StructType type, ArrayList<TreeNode> list)
    {
        StructNode node;
        switch (type)
        {
            case And_Add: node = getAndNode(list); break;
            case Or_Subtract: node = getOrNode(list); break;
            case Not_Negate: node = getNotNode(list); break;
            default: node = null; break;
        }
        return node;
    }

    private static StructNode getNotNode(TreeNode node)
    {
        if(node==null)
            return null;
        ArrayList<TreeNode> list = new ArrayList<>();
        list.add(node);
        return new NotNode(list);
    }

    private static StructNode getNotNode(ArrayList<TreeNode> list)
    {
        if(list.size()<1 || list.get(0)==null)
            return null;
        return new NotNode(list);
    }

    private static StructNode getOrNode(TreeNode node)
    {
        if(node==null)
            return null;
        ArrayList<TreeNode> list = new ArrayList<>();
        list.add(node);
        return new OrNode(list);
    }

    private static StructNode getOrNode(ArrayList<TreeNode> list)
    {
        if(list.size()<1 || list.get(0)==null)
            return null;
        return new OrNode(list);
    }

    private static StructNode getAndNode(TreeNode node)
    {
        if(node==null)
            return null;
        ArrayList<TreeNode> list = new ArrayList<>();
        list.add(node);
        return new AndNode(list);
    }

    private static StructNode getAndNode(ArrayList<TreeNode> list)
    {
        if(list.size()<1 || list.get(0)==null)
            return null;
        return new AndNode(list);
    }

    private static DataNode getBoolNode(String text)
    {
        return new BoolNode(text);
    }

    private static DataNode getBoolNode(ArrayList<SearchResult<Boolean>> b)
    {
        return new BoolNode(b);
    }

    private static DataNode getDoubleNode(String text)
    {
        return new DoubleNode(text);
    }

    private static DataNode getDoubleNode(ArrayList<SearchResult<Double>> d)
    {
        return new DoubleNode(d);
    }
}
