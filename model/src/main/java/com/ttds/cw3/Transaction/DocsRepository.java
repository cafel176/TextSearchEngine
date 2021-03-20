package com.ttds.cw3.Transaction;

import com.ttds.cw3.DB.DocDB;
import com.ttds.cw3.DB.DvectorDB;
import com.ttds.cw3.DB.TvectorDB;
import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Data.DocVector;
import com.ttds.cw3.Data.TermVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public final class DocsRepository
{
    private DocDB docDB;
    private DvectorDB dvs;
    private TvectorDB terms;

    @Autowired
    public DocsRepository(DocDB docDB, DvectorDB dvDB, TvectorDB tvDB)
    {
        this.docDB = docDB;
        this.dvs = dvDB;
        this.terms = tvDB;
    }

    public void addDocVector(String docid,String docName, String term)
    {
        if(dvs.exists(docid))
        {
            DocVector dv = dvs.find(docid);
            dv.addTerm(term);
            dvs.save(dv);
        }
        else{
            DocVector dv = new DocVector(docid,docName);
            dv.addTerm(term);
            dvs.insert(dv);
        }
    }

    public void addTermVector(String term, String docid, int pos)
    {
        if(terms.exists(term))
        {
            TermVector t = terms.find(term);
            t.addPos(docid,pos);
            terms.save(t);
        }
        else
        {
            TermVector t = new TermVector(term);
            t.addPos(docid, pos);
            terms.insert(t);
        }
    }

    public Doc addDoc(Doc doc)
    {
        return docDB.insert(doc);
    }

    public Doc getDoc(String id)
    {
        return docDB.findById(id);
    }

    public boolean containDoc(String id)
    {
        return docDB.existsById(id);
    }

    public long countDoc()
    {
        return docDB.count();
    }

    public DocVector getDvByDocid(String docid){return dvs.find(docid);}

    public TermVector getTermByTerm(String term){return terms.find(term);}

    public List<TermVector> getTerms(){return terms.findAll();}

    public List<DocVector> getDvs(){return dvs.findAll();}

    public List<Doc> getDocs(){return docDB.findAll();}

    public long getDocSize(){return docDB.count();}

    public long getTermsSize(){return terms.count();}

    public void clear()
    {
        docDB.deleteAll();
        dvs.deleteAll();
        terms.deleteAll();
    }
}
