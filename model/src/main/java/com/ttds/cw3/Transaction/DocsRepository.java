package com.ttds.cw3.Transaction;

import com.ttds.cw3.DB.DocDB;
import com.ttds.cw3.DB.DvectorDB;
import com.ttds.cw3.DB.TvectorDB;
import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Data.DocVector;
import com.ttds.cw3.Data.TermVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class DocsRepository
{
    private DocDB docDB;
    private DvectorDB dvsDB;
    private TvectorDB termsDB;

    private ConcurrentHashMap<String, Doc> docsId;
    private ConcurrentHashMap<String, DocVector> dvs;
    private ConcurrentHashMap<String, TermVector> terms;

    private List<DocVector> dvcache1;
    private List<DocVector> dvcache2;
    private int dvInUse = 0;
    private boolean dvCanUse = true;

    private ConcurrentHashMap<String,Doc> docCacheMap1;
    private ConcurrentHashMap<String,Doc> docCacheMap2;
    private int docInUse = 0;
    private boolean docCanUse = true;

    private int max = 30000;

    @Autowired
    public DocsRepository(DocDB docDB, DvectorDB dvDB, TvectorDB tvDB)
    {
        this.docDB = docDB;
        this.dvsDB = dvDB;
        this.termsDB = tvDB;

        docsId = new ConcurrentHashMap<>();
        dvs = new ConcurrentHashMap<>();
        terms = new ConcurrentHashMap<>();
        docCacheMap1 = new ConcurrentHashMap<>();
        docCacheMap2 = new ConcurrentHashMap<>();
    }

    public void setMax(int max)
    {
        this.max = max;

        dvcache1 = dvsDBfind(0,max);
        List list = docDBfind(0,max);
        for(int i=0;i<list.size();i++)
        {
            Doc doc = (Doc)list.get(i);
            docCacheMap1.put(doc.getId(),doc);
        }
    }

// ============================ 数据库操作 ================================

    public long getDocDBSize()
    {
        return docDB.count();
    }

    public long getDvsDBSize(){
        return dvsDB.count();
    }

    public long getTermsDBSize(){return termsDB.count();}

    public void pushDB(int thread,int i)
    {
        int num = 0,ori = 0;
        ArrayList list;
        if(i==2)
        {
            System.out.println("terms数据上传:");
            ori = (int)termsDB.count();
            num = terms.size();
            list = new ArrayList(terms.values());
        }
        else if(i==1)
        {
            System.out.println("dvs数据上传:");
            ori = (int)dvsDB.count();
            num = dvs.size();
            list = new ArrayList(dvs.values());
        }
        else
        {
            System.out.println("docs数据上传:");
            ori = (int)docDB.count();
            num = docsId.size();
            list = new ArrayList(docsId.values());
        }

        long startTime = System.currentTimeMillis();
        if(thread<=0)
            inThread(ori,num,0, num,i,list);
        else
        {
            ExecutorService pool = Executors.newCachedThreadPool();
            int per = num/thread;
            if(per < 1)
                per = 1;
            for (int s = 0, e = per; e <= num;)
            {
                int finalS = s, finalE = e;
                int finalNum = num;
                int finalI = i;
                int finalOri = ori;
                pool.execute(new Runnable() {
                    @Override
                    public void run()
                    {
                        final int start = finalS,end = finalE;
                        final int type = finalI,size = finalNum;
                        inThread(finalOri,size,start, end,type,list);
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

        long endTime = System.currentTimeMillis();
        System.out.println();
        System.out.println("用时："+(endTime-startTime));

        if(i==2)
        {
            terms.clear();
        }
        else if(i==1)
        {
            dvs.clear();
        }
        else
        {
            docsId.clear();
        }
    }

    private void inThread(int ori,int size, int start,int end,int type,ArrayList list)
    {
        for(int i=start;i<end;i++)
        {
            if(type==2)
            {
                TermVector t = (TermVector)list.get(i);
                synchronized(termsDB)
                {
                    termsDB.save(t);
                }
                System.out.print("\b\b\b\b\b\b\b"+String.format("%.2f",100.0*i/size)+"%");
            }
            else if(type==1)
            {
                DocVector dv = (DocVector) list.get(i);
                synchronized(dvsDB)
                {
                    dvsDB.insert(dv);
                }
                System.out.print("\b\b\b\b\b\b\b"+String.format("%.2f",100.0*(dvsDB.count()-ori)/size)+"%");
            }
            else
            {
                Doc d = (Doc)list.get(i);
                synchronized(docDB)
                {
                    docDB.insert(d);
                }
                System.out.print("\b\b\b\b\b\b\b"+String.format("%.2f",100.0*(docDB.count()-ori)/size)+"%");
            }
        }
    }

    public List<DocVector> dvsDBfindCache(int pageNo, int pageSize,int all)
    {
        if(dvCanUse==false)
            return null;
        if(dvInUse==1)
        {
            dvInUse=2;
        }
        else
        {
            dvInUse=1;
        }
        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(new Runnable() {
            @Override
            public void run()
            {
                if(pageNo==all-1)
                    inThread(0,max);
                else
                    inThread(pageNo+1,pageSize);
            }
        });
        if(dvInUse==1)
            return dvcache1;
        else
            return dvcache2;
    }

    public List<DocVector> dvsDBfind(int pageNo, int pageSize){return dvsDB.find(pageNo,pageSize);}

    public ConcurrentHashMap<String,Doc> docDBfindCache(int pageNo, int pageSize,int all)
    {
        if(docCanUse==false)
            return null;
        if(docInUse==1)
        {
            docInUse=2;
        }
        else
        {
            docInUse=1;
        }

        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(new Runnable() {
            @Override
            public void run()
            {
                if(pageNo==all-1)
                    inThread2(0,max);
                else
                    inThread2(pageNo+1,pageSize);
            }
        });
        if(docInUse==1)
            return docCacheMap1;
        else
            return docCacheMap2;
    }

    public List<Doc> docDBfind(int pageNo, int pageSize)
    {
        return docDB.find(pageNo,pageSize);
    }

    public List<TermVector> termcDBfind(int pageNo, int pageSize)
    {
        return termsDB.find(pageNo,pageSize);
    }

    // ============================ 缓存数据操作 ================================
    @CachePut(cacheNames = "dvMap",key = "#docid")
    public DocVector addDocVector(String docid,String docName, String term)
    {
        DocVector dv;
        if(dvs.containsKey(docid))
        {
            dvs.get(docid).addTerm(term);
            dv = dvs.get(docid);
        }
        else
        {
            dv = new DocVector(docid,docName);
            dv.addTerm(term);
            dvs.put(docid, dv);
        }
        return dv;
    }

    @CachePut(cacheNames = "termMap",key = "#term")
    public TermVector addTermVector(String term, String docid, int pos)
    {
        TermVector t;
        if(terms.containsKey(term))
        {
            terms.get(term).addPos(docid,pos);
            t = terms.get(term);
        }
        else if(termsDB.exists(term))
        {
            t = getTermByTerm(term);
            t.addPos(docid,pos);
            terms.put(term, t);
        }
        else
        {
            t = new TermVector(term);
            t.addPos(docid, pos);
            terms.put(term, t);
        }
        return t;
    }

    @CachePut(cacheNames = "docMap",key = "#id")
    public Doc addDoc(int id,Doc doc)
    {
        return docsId.put(Integer.toString(id),doc);
    }

    @Cacheable(cacheNames = "dvMap",key = "#docid",sync = true)
    public DocVector getDvByDocid(String docid)
    {
        return dvsDB.find(docid);
    }

    @Cacheable(cacheNames = "termMap",key = "#term",sync = true)
    public TermVector getTermByTerm(String term)
    {
        return termsDB.find(term);
    }

    @Cacheable(cacheNames = "docMap",key = "#id",sync = true)
    public Doc getDoc(String id)
    {
        return docDB.findById(id);
    }

    @Cacheable(cacheNames = "sizeMap",key = "#root.methodName",sync = true)
    public long getDocNum()
    {
        return dvsDB.count();
    }

    // ============================ 其他操作 ================================

    public boolean containDoc(String id)
    {
        return docsId.containsKey(id);
    }

    public long getDocSize(){return docsId.size();}

    public void clear()
    {
        docsId.clear();
        dvs.clear();
        terms.clear();
    }

    private void inThread(int pageNo, int pageSize)
    {
        dvCanUse = false;
        if(dvInUse==1)
        {
            dvcache2 = dvsDBfind(pageNo,pageSize);
        }
        else
        {
            dvcache1 = dvsDBfind(pageNo,pageSize);
        }
        dvCanUse = true;
    }

    private void inThread2(int pageNo, int pageSize)
    {
        docCanUse = false;
        if(docInUse==1)
        {
            docCacheMap2.clear();
            List list = docDBfind(pageNo,pageSize);
            for(int i=0;i<list.size();i++)
            {
                Doc doc = (Doc)list.get(i);
                docCacheMap2.put(doc.getId(),doc);
            }
        }
        else
        {
            docCacheMap1.clear();
            List list = docDBfind(pageNo,pageSize);
            for(int i=0;i<list.size();i++)
            {
                Doc doc = (Doc)list.get(i);
                docCacheMap1.put(doc.getId(),doc);
            }
        }
        docCanUse = true;
    }
}