package com.ttds.cw3.Transaction.BoolTree.StructNode;

import com.ttds.cw3.Transaction.BoolTree.DataNode.DataNode;
import com.ttds.cw3.Transaction.BoolTree.TreeNode;

import java.util.ArrayList;

public final class AndNode extends StructNode
{
    public AndNode(ArrayList<TreeNode> nodes)
    {
        super(nodes);
        nodeType = StructType.And_Add;
    }

    @Override
    protected void addNode(TreeNode n)
    {
        this.nodes.add(n);
    }

    @Override
    public DataNode getResult()
    {
        if(nodes.size()==1)
            return nodes.get(0).getResult();
        else
            return nodes.get(0).getResult().And_Add(nodes.get(1).getResult());
    }
}
