package com.ttds.cw3.Transaction.BoolTree;

import com.ttds.cw3.Transaction.BoolTree.DataNode.DataNode;
import com.ttds.cw3.Transaction.BoolTree.DataNode.DataType;
import com.ttds.cw3.Transaction.BoolTree.StructNode.StructType;

public abstract class TreeNode
{
    protected StructType nodeType;
    protected DataType dataType;

    public StructType getNodeType() {
        return nodeType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public abstract DataNode getResult();

    public abstract void setValue(Object value);
}
