package com.ttds.cw3.Strategy.SearchModule;

import java.util.ArrayList;

public class ProximitySearch extends PhraseSearch
{
    protected boolean order; // 两词之间距离

    public void setOrder(boolean order) {
        this.order = order;
    }

    public ProximitySearch(int limit, String pattern) {
        super(limit, pattern);
    }

    @Override
    protected void setParams(String param) throws Exception
    {
        order = true;

        try
        {
            super.setParams(param);
        }
        catch (Exception e) //参数不合理
        {
            throw new NumberFormatException("Proximity 运算参数异常");
        }
    }

    @Override
    protected int match(int pos, ArrayList<Integer> posList)
    {
        for(int i=0;i<posList.size();i++)
        {
            if(Math.abs(posList.get(i)-pos) <= dis && (!order||posList.get(i)>pos))
            {
                // 如果存在两个词位置在范围内则完成搜索
                return posList.get(i);
            }
        }
        return -1;
    }
}
