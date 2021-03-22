package com.ttds.cw3.DB;

import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Data.DocVector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocDB
{
    private DocRepository repository;

    @Autowired
    public DocDB(DocRepository repository)
    {
        this.repository = repository;
    }

    public List<Doc> findByCategory(String category)
    {
        return repository.findByCategory(category);
    }

    public List<Doc> findByAuthor(String author)
    {
        return repository.findByAuthor(author);
    }

    public Doc findById(String id)
    {
        Optional<Doc> opt = this.repository.findById(id);
        return opt.orElse(null);
    }

    public List<Doc> findAll()
    {
        return repository.findAll();
    }

    public List<Doc> find(int pageNo, int pageSize)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Doc> pages = this.repository.findAll(paging);
        List<Doc> list = pages.getContent();
        return list;
    }

    public boolean existsById(String id)
    {
        return this.repository.existsById(id);
    }

    public long count()
    {
        return this.repository.count();
    }

    public Doc save(Doc tv)
    {
        return this.repository.save(tv);
    }

    public Doc insert(Doc tv)
    {
        return this.repository.insert(tv);
    }

    public void deleteAll()
    {
        this.repository.deleteAll();
    }
}