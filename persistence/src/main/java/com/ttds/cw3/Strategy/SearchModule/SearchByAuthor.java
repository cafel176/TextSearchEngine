package com.ttds.cw3.Strategy.SearchModule;

import com.ttds.cw3.Adapter.DocAdapter;
import com.ttds.cw3.Adapter.DocVectorAdapter;
import com.ttds.cw3.Adapter.ModelManagerAdapter;
import com.ttds.cw3.Adapter.PreProcessingAdapter;
import com.ttds.cw3.Data.SearchResult;
import com.ttds.cw3.Interface.DocInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class SearchByAuthor extends SearchModule
{
    private ArrayList<String> docs = new ArrayList<>();

    public SearchByAuthor(int limit, int thread, String pattern)
    {
        super(limit,thread,pattern);
    }

    public SearchResult<Boolean>[] searchAllDoc(String str, String param, PreProcessingAdapter preProcessing, ModelManagerAdapter m) throws Exception
    {
        setParams(param);

        // 搜索输入
        ArrayList<String> words = new ArrayList();
        words.add(str);

        // 结果输出
        List dvs = m.getDvs();
        int size = dvs.size();
        SearchResult<Boolean>[] results = new SearchResult[size];

        // =============== 遍历文档集合 ===============
        final int max = 100000;
        int all = (int)Math.ceil(1.0*size/max);
        long startTime = System.currentTimeMillis();
        for(int k=0;k<all;k++)
        {
            int num = (k+1)*max;
            if(num>size)
                num = size;

            if(thread<=0)
                inThread(size,k*max, num, words,dvs,results, m);
            else
            {
                ExecutorService pool = Executors.newCachedThreadPool();
                int per = num/thread;
                if(per < 1)
                    per = 1;
                for (int s = k*max, e = k*max+per; e <= num;)
                {
                    ArrayList<String> finalWords = words;
                    int finalS = s, finalE = e;
                    pool.execute(new Runnable() {
                        @Override
                        public void run()
                        {
                            final int start = finalS,end = finalE;
                            inThread(size,start, end, finalWords,dvs,results, m);
                        }
                    });

                    if(e==num)
                        break;

                    s = e;
                    e += per;
                    if(e>num)
                        e = num;
                }
                pool.shutdown();
                try
                {
                    pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        long endTime = System.currentTimeMillis();
        System.out.println("Search搜索时间："+(endTime-startTime));

        return results;
    }

    @Override
    protected void inThread(int size, int start, int end, ArrayList<String> words, List dvs, SearchResult<Boolean>[] results, ModelManagerAdapter m)
    {
        synchronized(docs)
        {
            if(docs.isEmpty())
            {
                for (String word : words)
                {
                    ArrayList<DocInterface> list = m.getDocsByAuthor(word);
                    if(list==null)
                        break;
                    for(int i=0;i<list.size();i++)
                    {
                        docs.add(list.get(i).getId());
                    }
                }
            }
        }

        for(int j=start;j<end;j++)
        {
            DocVectorAdapter dv = new DocVectorAdapter(dvs.get(j));
            if(dv==null)
                continue;

            // =============== 筛选步骤 ===============
            if(docs.contains(dv.getDocid()))
                results[j] = searchDoc(words,dv,m.getDoc(dv.getDocid()),m);
            else
                results[j] = new SearchResult(dv.getDocid(),dv.getDocName(), "","",false);
        }
    }

    @Override
    protected SearchResult<Boolean> searchDoc(ArrayList<String> words, DocVectorAdapter doc, DocAdapter docinfo, ModelManagerAdapter m)
    {
        SearchResult<Boolean> re = new SearchResult(docinfo.getId(),docinfo.getName(),docinfo.getAuthor(),docinfo.getCategory(), true);
        String text = docinfo.getText();
        re.setDesc(getRelatedStr(0,text,pattern));
        re.setText(text);

        return re;
    }

    @Override
    protected void setParams(String param) throws Exception
    {

    }
}
