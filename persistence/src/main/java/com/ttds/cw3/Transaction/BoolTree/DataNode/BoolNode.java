package com.ttds.cw3.Transaction.BoolTree.DataNode;

import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;

public final class BoolNode extends DataNode<Boolean>
{
    public BoolNode(ArrayList<SearchResult<Boolean>> value)
    {
        super(value);
        dataType = DataType.Boolean;
    }

    public BoolNode(String text)
    {
        super(text);
        dataType = DataType.Boolean;
    }

    @Override
    public BoolNode And_Add(DataNode<Boolean> other)
    {
        ArrayList<SearchResult<Boolean>> result = new ArrayList<>();
        ArrayList<SearchResult<Boolean>> otherValue = other.getValue();
        for(int i=0;i<value.size();i++)
        {
            SearchResult<Boolean> a = value.get(i);
            if(a==null)
                continue;
            SearchResult re = new SearchResult(a.getDocid(),a.getDocName(),a.getAuthor(),a.getCategory(),a.getValue() && otherValue.get(i).getValue());
            re.setDesc(a.getDesc());
            result.add(re);
        }
        return new BoolNode(result);
    }

    @Override
    public BoolNode Or_Subtract(DataNode<Boolean> other)
    {
        ArrayList<SearchResult<Boolean>> result = new ArrayList<>();
        ArrayList<SearchResult<Boolean>> otherValue = other.getValue();
        for(int i=0;i<value.size();i++)
        {
            SearchResult<Boolean> a = value.get(i);
            if(a==null)
                continue;
            SearchResult re = new SearchResult(a.getDocid(),a.getDocName(),a.getAuthor(),a.getCategory(),a.getValue() || otherValue.get(i).getValue());
            re.setDesc(a.getDesc());
            result.add(re);
        }
        return new BoolNode(result);
    }

    @Override
    public BoolNode Not_Negate()
    {
        ArrayList<SearchResult<Boolean>> result = new ArrayList<>();
        for(int i=0;i<value.size();i++)
        {
            SearchResult<Boolean> a = value.get(i);
            if(a==null)
                continue;
            SearchResult re = new SearchResult(a.getDocid(),a.getDocName(),a.getAuthor(),a.getCategory(),!a.getValue());
            re.setDesc(a.getDesc());
            result.add(re);
        }
        return new BoolNode(result);
    }

    @Override
    public BoolNode Default()
    {
        for(int i=0;i<value.size();i++)
        {
            SearchResult<Boolean> a = value.get(i);
            if(a==null)
                continue;
            String t = a.getDesc();
            if(t==null||t.isEmpty())
                t = "Empty";
            a.setDesc(t);
        }
        return this;
    }
}
