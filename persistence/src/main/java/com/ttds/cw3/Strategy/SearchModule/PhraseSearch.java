package com.ttds.cw3.Strategy.SearchModule;

import com.ttds.cw3.Adapter.DataAdapter;
import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Data.SearchResult;

import java.util.ArrayList;

public class PhraseSearch extends SearchModule
{
    protected int dis; // 两词之间距离

    public void setDis(int dis) {
        this.dis = dis;
    }

    public PhraseSearch(int limit, String pattern) {
        super(limit, pattern);
    }

    @Override
    protected SearchResult<Boolean> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DataAdapter data, DocAdapter docinfo)
    {

        // =============== 匹配前两个词位置 ===============
        int pos = -1;
        String docid = doc.getDocid();
        if(words.size()>1)
        {
            ArrayList<Integer> posList1 = data.getTerm(words.get(0)).getPostings().get(docid);
            ArrayList<Integer> posList2 = data.getTerm(words.get(1)).getPostings().get(docid);
            for(int i=0;i<posList1.size();i++)
            {
                pos = match(posList1.get(i),posList2);
                if(pos>0)
                    break;
            }

            // 匹配失败
            if(pos<0)
                return new SearchResult(docid,false);
        }
        else
        {
            pos = data.getTerm(words.get(0)).getPostings().get(docid).get(0);
        }

        // =============== 匹配其他词 ===============
        for(int i=2;i<words.size();i++)
        {
            ArrayList<Integer> posList = data.getTerm(words.get(i)).getPostings().get(docid);
            pos = match(pos,posList);
            // 如果不存在，结束搜索
            if(pos<0)
                return new SearchResult(docid,false);
        }

        SearchResult<Boolean> re = new SearchResult(docid,true);
        String text = docinfo.getText();
        pos = data.getTerm(words.get(0)).getPostings().get(docid).get(0);
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
