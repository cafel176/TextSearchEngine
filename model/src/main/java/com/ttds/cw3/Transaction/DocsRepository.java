package com.ttds.cw3.Transaction;

import com.ttds.cw3.DB.DocDB;
import com.ttds.cw3.DB.DvectorDB;
import com.ttds.cw3.DB.TvectorDB;
import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Data.DocVector;
import com.ttds.cw3.Data.TermVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public final class DocsRepository
{
    private DocDB docDB;
    private DvectorDB dvsDB;
    private TvectorDB termsDB;

    private ConcurrentHashMap<String, Doc> docsId;
    private ConcurrentHashMap<String, ArrayList<Doc>> docsCategory;
    private ConcurrentHashMap<String, ArrayList<Doc>> docsAuthor;
    private ConcurrentHashMap<String, DocVector> dvs;
    private ConcurrentHashMap<String, TermVector> terms;

    @Autowired
    public DocsRepository(DocDB docDB, DvectorDB dvDB, TvectorDB tvDB)
    {
        this.docDB = docDB;
        this.dvsDB = dvDB;
        this.termsDB = tvDB;

        docsId = new ConcurrentHashMap<>();
        docsCategory = new ConcurrentHashMap<>();
        docsAuthor = new ConcurrentHashMap<>();
        dvs = new ConcurrentHashMap<>();
        terms = new ConcurrentHashMap<>();
    }

    // ============================ 数据库操作 ================================
    public long getDocDBSize(){return docDB.count();}

    public long getDvsDBSize(){
        return dvsDB.count();
    }

    public long getTermsDBSize(){return termsDB.count();}

    public ArrayList<Doc> getDocsByIdFromDB(int pageNo, int pageSize)
    {
        return new ArrayList<Doc>(docDB.find(pageNo, pageSize));
    }

    public ArrayList<Doc> getDocsByCategoryFromDB(int pageNo, int pageSize)
    {
        return new ArrayList<>(docDB.find(pageNo, pageSize));
    }

    public ArrayList<Doc> getDocsByAuthorFromDB(int pageNo, int pageSize)
    {
        return new ArrayList<>(docDB.find(pageNo, pageSize));
    }

    public void pushDB(int thread,int i)
    {
        int num = 0,ori = 0;
        ArrayList list,keys = null;
        if(i==2)
        {
            System.out.println("terms数据上传:");
            ori = (int)termsDB.count();
            num = terms.size();
            list = new ArrayList(terms.values());
            keys = new ArrayList(terms.keySet());
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
            inThread(ori,num,0, num,i,list,keys);
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
                ArrayList finalKeys = keys;
                int finalOri = ori;
                pool.execute(new Runnable() {
                    @Override
                    public void run()
                    {
                        final int start = finalS,end = finalE;
                        final int type = finalI,size = finalNum;
                        inThread(finalOri,size,start, end,type,list, finalKeys);
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

    private void inThread(int ori,int size, int start,int end,int type,ArrayList list,ArrayList keys)
    {
        for(int i=start;i<end;i++)
        {
            if(type==2)
            {
                String key = (String)keys.get(i);
                TermVector r = (TermVector)list.get(i);
                synchronized(termsDB)
                {
                    if(termsDB.exists(key))
                    {
                        TermVector t = termsDB.find(key);
                        t.addPostings(r.getPostings());
                        termsDB.save(t);
                    }
                    else
                        termsDB.insert(r);
                }
                System.out.print("\b\b\b\b\b\b\b"+String.format("%.2f",100.0*i/size)+"%");
            }
            else if(type==1)
            {
                synchronized(dvsDB)
                {
                    dvsDB.insert((DocVector) list.get(i));
                }
                System.out.print("\b\b\b\b\b\b\b"+String.format("%.2f",100.0*(dvsDB.count()-ori)/size)+"%");
            }
            else
            {
                synchronized(docDB)
                {
                    docDB.insert((Doc)list.get(i));
                }
                System.out.print("\b\b\b\b\b\b\b"+String.format("%.2f",100.0*(docDB.count()-ori)/size)+"%");
            }
        }
    }

    public void initFromDB()
    {
        int max = 5000;
        System.out.println("dvs数据读取:");
        long size = dvsDB.count();
        int all = (int)Math.ceil(1.0*size/max);
        long startTime = System.currentTimeMillis();

        List list;
        DocVector doc;
        for(int k=0;k<all;k++)
        {
            list = dvsDB.find(k, max);
            for(int i=0;i<list.size();i++)
            {
                doc = (DocVector) list.get(i);
                dvs.put(doc.getDocid(),doc);
                System.out.print("\b\b\b\b\b\b\b"+String.format("%.2f",100.0*(k*max+i)/size)+"%");
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println();
        System.out.println("数据量:"+dvs.size());
        System.out.println("用时："+(endTime-startTime));

        max = 5000;
        System.out.println("terms数据读取:");
        size = termsDB.count();
        all = (int)Math.ceil(1.0*size/max);
        TermVector t;
        for(int k=0;k<all;k++)
        {
            list = termsDB.find(k, max);
            for(int i=0;i<list.size();i++)
            {
                t = (TermVector)list.get(i);
                terms.put(t.getTerm(),t);
                System.out.print("\b\b\b\b\b\b\b"+String.format("%.2f",100.0*(k*max+i)/size)+"%");
            }
        }
        endTime = System.currentTimeMillis();
        System.out.println();
        System.out.println("数据量:"+terms.size());
        System.out.println("用时："+(endTime-startTime));

        max = 5000;
        System.out.println("docs数据读取:");
        size = docDB.count();
        all = (int)Math.ceil(1.0*size/max);
        Doc d;
        for(int k=0;k<all;k++)
        {
            list = docDB.find(k, max);
            for(int i=0;i<list.size();i++)
            {
                d = (Doc)list.get(i);
                docsId.put(d.getId(),d);
                if(docsCategory.containsKey(d.getCategory()))
                    docsCategory.get(d.getCategory()).add(d);
                else
                {
                    ArrayList<Doc> docs = new ArrayList<>();
                    docs.add(d);
                    docsCategory.put(d.getCategory(),docs);
                }
                if(docsAuthor.containsKey(d.getAuthor()))
                    docsAuthor.get(d.getAuthor()).add(d);
                else
                {
                    ArrayList<Doc> docs = new ArrayList<>();
                    docs.add(d);
                    docsAuthor.put(d.getAuthor(),docs);
                }
                System.out.print("\b\b\b\b\b\b\b"+String.format("%.2f",100.0*(k*max+i)/size)+"%");
            }
        }
        endTime = System.currentTimeMillis();
        System.out.println();
        System.out.println("数据量:"+docsId.size());
        System.out.println("用时："+(endTime-startTime));
    }
    // ============================ 缓存数据操作 ================================

    public void addDocVector(String docid,String docName, String term)
    {
        if(dvs.containsKey(docid))
            dvs.get(docid).addTerm(term);
        else
        {
            DocVector dv = new DocVector(docid,docName);
            dv.addTerm(term);
            dvs.put(docid, dv);
        }
    }

    public void addTermVector(String term, String docid, int pos)
    {
        if(terms.containsKey(term))
            terms.get(term).addPos(docid,pos);
        else
        {
            TermVector t = new TermVector(term);
            t.addPos(docid, pos);
            terms.put(term, t);
        }
    }

    public DocVector getDvByDocid(String docid){return dvs.get(docid);}

    public TermVector getTermByTerm(String term){return terms.get(term);}

    public Doc getDoc(String id)
    {
        return docsId.get(id);
    }

    public ArrayList<Doc> getDocsByCategory(String category)
    {
        return docsCategory.get(category);
    }

    public ArrayList<Doc> getDocsByAuthor(String author)
    {
        return docsAuthor.get(author);
    }

    public ArrayList<TermVector> getTerms(){return new ArrayList<>(terms.values());}

    public ArrayList<DocVector> getDvs(){return new ArrayList<>(dvs.values());}

    public Doc addDoc(int id,Doc doc)
    {
        return docsId.put(Integer.toString(id),doc);
    }

    public boolean containDoc(String id)
    {
        return docsId.containsKey(id);
    }

    public long getDocSize(){return docsId.size();}

    public long getDvsSize(){
        return dvs.size();
    }

    public long getTermsSize(){return terms.size();}

    public void clear()
    {
        docsId.clear();
        docsCategory.clear();
        docsAuthor.clear();
        dvs.clear();
        terms.clear();
    }
}
