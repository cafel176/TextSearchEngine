package com.ttds.cw3.DB;

import com.ttds.cw3.Data.DocVector;
import com.ttds.cw3.Data.TermVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DvectorDB
{
    private DvectorRepository repository;

    @Autowired
    public DvectorDB(DvectorRepository repository)
    {
        this.repository = repository;
    }

    public DocVector find(String docid)
    {
        Optional<DocVector> opt = this.repository.findById(docid);
        return opt.orElse(null);
    }

    public List<DocVector> findAll()
    {
        List<DocVector> list = this.repository.findAll();
        return list;
    }

    public List<DocVector> find(int pageNo, int pageSize)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<DocVector> pages = this.repository.findAll(paging);
        List<DocVector> list = pages.getContent();
        return list;
    }

    public boolean exists(String docid)
    {
        return this.repository.existsById(docid);
    }

    public long count()
    {
        return this.repository.count();
    }

    public DocVector insert(DocVector dv)
    {
        return this.repository.insert(dv);
    }

    public DocVector save(DocVector dv)
    {
        return this.repository.save(dv);
    }

    public void deleteAll()
    {
        this.repository.deleteAll();
    }
}
