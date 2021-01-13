package com.ttds.cw3.Transaction.BoolTree.StructNode;

import com.ttds.cw3.Transaction.BoolTree.DataNode.DataNode;
import com.ttds.cw3.Transaction.BoolTree.TreeNode;

import java.util.ArrayList;

public final class OrNode extends StructNode
{
    public OrNode(ArrayList<TreeNode> nodes)
    {
        super(nodes);
        nodeType = StructType.Or_Subtract;
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
            return nodes.get(0).getResult().Or_Subtract(nodes.get(1).getResult());
    }
}
