package com.ttds.cw3.Transaction.BoolTree.StructNode;

import com.ttds.cw3.Transaction.BoolTree.TreeNode;

import java.util.ArrayList;

public abstract class StructNode extends TreeNode
{
    protected ArrayList<TreeNode> nodes;

    public StructNode(ArrayList<TreeNode> nodes)
    {
        this.nodes = nodes;

        dataType = this.nodes.get(0).getDataType();
    }

    @Override
    public void setValue(Object value)
    {
        addNode((TreeNode)value);
    }

    protected abstract void addNode(TreeNode value);
}
