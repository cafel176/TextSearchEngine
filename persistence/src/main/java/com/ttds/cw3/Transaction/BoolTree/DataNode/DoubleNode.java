package com.ttds.cw3.Transaction.BoolTree.DataNode;

import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;

public final class DoubleNode extends DataNode<Double>
{
    public DoubleNode(ArrayList<SearchResult<Double>> value)
    {
        super(value);
        dataType = DataType.Double;
    }

    public DoubleNode(String text)
    {
        super(text);
        dataType = DataType.Double;
    }

    @Override
    public DoubleNode And_Add(DataNode<Double> other)
    {
        ArrayList<SearchResult<Double>> result = new ArrayList<>();
        ArrayList<SearchResult<Double>> otherValue = other.getValue();
        for(int i=0;i<value.size();i++)
        {
            SearchResult<Double> a = value.get(i);
            if(a==null)
                continue;
            Double d = a.getValue() + otherValue.get(i).getValue();
            SearchResult re = new SearchResult(a.getDocid(),a.getDocName(),d);
            re.setDesc(Double.toString(d));
            result.add(re);
        }
        return new DoubleNode(result);
    }

    @Override
    public DoubleNode Or_Subtract(DataNode<Double> other)
    {
        ArrayList<SearchResult<Double>> result = new ArrayList<>();
        ArrayList<SearchResult<Double>> otherValue = other.getValue();
        for(int i=0;i<value.size();i++)
        {
            SearchResult<Double> a = value.get(i);
            if(a==null)
                continue;
            Double d = a.getValue() - otherValue.get(i).getValue();
            SearchResult re = new SearchResult(a.getDocid(),a.getDocName(),d);
            re.setDesc(Double.toString(d));
            result.add(re);
        }
        return new DoubleNode(result);
    }

    @Override
    public DoubleNode Not_Negate()
    {
        ArrayList<SearchResult<Double>> result = new ArrayList<>();
        for(int i=0;i<value.size();i++)
        {
            SearchResult<Double> a = value.get(i);
            if(a==null)
                continue;
            Double d = -a.getValue();
            SearchResult re = new SearchResult(a.getDocid(),a.getDocName(),d);
            re.setDesc(Double.toString(d));
            result.add(re);
        }
        return new DoubleNode(result);
    }

    @Override
    public DoubleNode Default()
    {
        for(int i=0;i<value.size();i++)
        {
            SearchResult<Double> a = value.get(i);
            if(a==null)
                continue;
            value.get(i).setDesc(Double.toString(value.get(i).getValue()));
        }
        return this;
    }
}
