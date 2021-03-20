package com.ttds.cw3.DB;

import com.ttds.cw3.Data.DocVector;
import com.ttds.cw3.Data.TermVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TvectorDB
{
    private TvectorRepository repository;

    @Autowired
    public TvectorDB(TvectorRepository repository)
    {
        this.repository = repository;
    }

    public TermVector find(String term)
    {
        Optional<TermVector> opt = this.repository.findById(term);
        return opt.orElse(null);
    }

    public List<TermVector> findAll()
    {
        List<TermVector> list = this.repository.findAll();
        return list;
    }

    public boolean exists(String term)
    {
        return this.repository.existsById(term);
    }

    public long count()
    {
        return this.repository.count();
    }

    public TermVector insert(TermVector tv)
    {
        return this.repository.insert(tv);
    }

    public TermVector save(TermVector tv)
    {
        return this.repository.save(tv);
    }

    public void deleteAll()
    {
        this.repository.deleteAll();
    }
}
