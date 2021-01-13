package com.ttds.cw3.Adapter;

import com.ttds.cw3.Interface.OtherParamsInterface;

public class OtherParamsAdapter
{
    private OtherParamsInterface param;

    public OtherParamsAdapter(OtherParamsInterface param) {
        this.param = param;
    }

    public void setParam(OtherParamsInterface param) {
        this.param = param;
    }

    public int getNum()
    {
        return param.getNum();
    }

}
