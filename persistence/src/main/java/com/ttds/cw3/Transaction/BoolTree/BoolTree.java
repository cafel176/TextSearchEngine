package com.ttds.cw3.Transaction.BoolTree;

import com.ttds.cw3.Transaction.BoolTree.DataNode.DataNode;
import com.ttds.cw3.Transaction.BoolTree.DataNode.DataType;
import com.ttds.cw3.Transaction.BoolTree.StructNode.StructNode;
import com.ttds.cw3.Factory.TreeNodeFactory;
import com.ttds.cw3.Transaction.Symbols.SymbolManager;
import com.ttds.cw3.Transaction.Symbols.StructSymbolType;

import java.util.ArrayList;
import java.util.Stack;

public final class BoolTree<T extends Object>
{
    private TreeNode root;
    private ArrayList<DataNode<T>> datas;

    public ArrayList<String> getDatas()
    {
        ArrayList<String> txts = new ArrayList<>();
        for(int i=0;i<datas.size();i++)
            txts.add(datas.get(i).getText());
        return txts;
    }

    public void setData(int i,ArrayList<T> value)
    {
        datas.get(i).setValue(value);
    }

    public ArrayList<T> getResult(DataType dataType)
    {
        ArrayList<T> arr;
        switch (dataType)
        {
            case Boolean: arr = root.getResult().getValue(); break;
            case Double: arr = root.getResult().getValue(); break;
            default: arr = null; break;
        }
        return arr;
    }

    public void buildTree(DataType dataType, ArrayList<String> query, SymbolManager symbols) throws Exception
    {
        datas = new ArrayList<>();

        Stack<String> stack = new Stack<>();
        ArrayList<TreeNode> all = new ArrayList<>();
        String nodeHint = "$node#";

        for(String str:query)
        {
            // 这个分支是当前项是操作符号的情况
            StructSymbolType type = symbols.TxtToSymbolDouble(str);
            if(type!= StructSymbolType.other)
            {
                int right,left;
                try
                {
                    String op2 = stack.pop();
                    right = Integer.parseInt(op2.split("#",2)[1]);
                    if(right<0)
                        throw new NumberFormatException();
                }
                catch (Exception e)
                {
                    throw new Exception(str+" 运算符参数异常！");
                }

                try
                {
                    String op1 = stack.pop();
                    left = Integer.parseInt(op1.split("#",2)[1]);
                    if(left<0)
                        throw new NumberFormatException();
                }
                catch (Exception e)
                {
                    throw new Exception(str+" 运算符参数异常！");
                }

                // 添加双操作数的结构节点
                ArrayList<TreeNode> nodes = new ArrayList<>();
                if(left>=0)
                    nodes.add(all.get(left));
                if(right>=0)
                    nodes.add(all.get(right));
                StructNode t = TreeNodeFactory.getStructNode(TreeNodeFactory.SymbolToType(type),nodes);

                stack.push(nodeHint+all.size());
                all.add(t);
            }
            else
            {
                String[] st = str.split(" ",2);
                String operate= st[0];

                String data = "";
                if(st.length>1)
                    data = st[1];

                type = symbols.TxtToSymbolSingle(operate);
                if(type!= StructSymbolType.other)
                {
                    if(data.isEmpty())
                        throw new Exception(operate+" 运算符参数异常！");

                    DataNode<T> t = TreeNodeFactory.getDataNode(dataType, data);
                    datas.add(t);

                    // 添加单操作数的结构节点
                    ArrayList<TreeNode> nodes = new ArrayList<>();
                    nodes.add(t);
                    StructNode t2 = TreeNodeFactory.getStructNode(TreeNodeFactory.SymbolToType(type),nodes);

                    stack.push(nodeHint+all.size());
                    all.add(t2);
                }
                else
                {
                    // 如果是操作数先直接入栈
                    DataNode<T> t = TreeNodeFactory.getDataNode(dataType, str);
                    datas.add(t);

                    stack.push(nodeHint+all.size());
                    all.add(t);
                }
            }
        }

        root = all.get(all.size()-1);
    }
}
