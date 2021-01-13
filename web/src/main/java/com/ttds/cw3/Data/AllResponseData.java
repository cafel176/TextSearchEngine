package com.ttds.cw3.Data;

import java.util.ArrayList;

public class AllResponseData
{
    private ResponseType type;
    private String hint;

    private int num;
    private ArrayList<ResponseData> datas;

    public AllResponseData(ResponseType type, String hint) {
        this.type = type;
        this.hint = hint;
        this.num = 0;
        this.datas = new ArrayList<>();
    }

    public AllResponseData()
    {
        this.type = ResponseType.success;
        this.hint = "success";
        this.num = 0;
        this.datas = new ArrayList<>();
    }

    public void setDatas(ArrayList<ResponseData> datas) {
        this.datas = datas;
    }

    public void addData(ResponseData data)
    {
        datas.add(data);
    }

    public ArrayList<ResponseData> getDatas() {
        return datas;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ResponseType getType() {
        return type;
    }

    public String getHint() {
        return hint;
    }
}
