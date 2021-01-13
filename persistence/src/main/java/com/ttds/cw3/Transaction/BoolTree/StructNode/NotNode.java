package com.ttds.cw3.Transaction.BoolTree.StructNode;

import com.ttds.cw3.Transaction.BoolTree.DataNode.DataNode;
import com.ttds.cw3.Transaction.BoolTree.TreeNode;

import java.util.ArrayList;

public final class NotNode extends StructNode
{
    public NotNode(ArrayList<TreeNode> nodes)
    {
        super(nodes);

        nodeType = StructType.Not_Negate;
    }

    @Override
    protected void addNode(TreeNode n)
    {
        this.nodes.add(n);
    }

    @Override
    public DataNode getResult()
    {
        return nodes.get(0).getResult().Not_Negate();
    }
}
