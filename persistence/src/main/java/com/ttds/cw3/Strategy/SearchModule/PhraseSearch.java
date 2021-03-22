package com.ttds.cw3.Strategy.SearchModule;

import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.ModelManagerAdapter;
import com.ttds.cw3.Adapter.TermVectorAdapter;
import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;

public class PhraseSearch extends SearchModule
{
    protected int dis; // 两词之间距离

    public void setDis(int dis) {
        this.dis = dis;
    }

    public PhraseSearch(int limit,int thread, String pattern) {
        super(limit,thread, pattern);
    }

    @Override
    protected SearchResult<Boolean> searchDoc(ArrayList<TermVectorAdapter> tvs, DocAdapter doc)
    {
        // =============== 匹配前两个词位置 ===============
        int pos = -1;
        String docid = doc.getId();
        if(tvs.size()>1)
        {
            ArrayList<Integer> posList1 = tvs.get(0).getPostings().get(docid);
            ArrayList<Integer> posList2 = tvs.get(1).getPostings().get(docid);
            for(int i=0;i<posList1.size();i++)
            {
                pos = match(posList1.get(i),posList2);
                if(pos>0)
                    break;
            }

            // 匹配失败
            if(pos<0)
                return new SearchResult(docid,doc.getName(),false);
        }
        else
        {
            pos = tvs.get(0).getPostings().get(docid).get(0);
        }

        // =============== 匹配其他词 ===============
        for(int i=2;i<tvs.size();i++)
        {
            ArrayList<Integer> posList = tvs.get(i).getPostings().get(docid);
            pos = match(pos,posList);
            // 如果不存在，结束搜索
            if(pos<0)
                return new SearchResult(docid,doc.getName(),false);
        }

        SearchResult<Boolean> re = new SearchResult(docid,doc.getName(),true);
        String text = doc.getText();
        pos = tvs.get(0).getPostings().get(docid).get(0);
        re.setDesc(getRelatedStr(pos,text,pattern));

        return re;
    }

    @Override
    protected void setParams(String param) throws Exception
    {
        if(param.isEmpty())
            return;

        String tokenParam = "_";
        String[] ops = param.split(tokenParam);
        try
        {
            if(ops.length==0 || ops[0].isEmpty())
            {
                throw new NumberFormatException();
            }

            dis = Integer.parseInt(ops[0]);
            if(dis<1)
                dis = 1;
        }
        catch (NumberFormatException e) //参数不合理
        {
            throw new NumberFormatException("Phrase 运算参数异常");
        }
    }

    protected int match(int pos, ArrayList<Integer> posList)
    {
        for(int i=0;i<posList.size();i++)
        {
            if(posList.get(i)-pos == 1)
            {
                // 如果存在两个词位置按顺序相邻则完成搜索
                return posList.get(i);
            }
        }
        return -1;
    }
}
