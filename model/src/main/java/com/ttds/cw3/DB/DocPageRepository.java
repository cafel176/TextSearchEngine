package com.ttds.cw3.DB;

import com.ttds.cw3.Data.Doc;
import com.ttds.cw3.Data.DocVector;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocPageRepository extends PagingAndSortingRepository<Doc,String>
{
    Page<Doc> findByAuthor (String author, Pageable pageable);
    Page<Doc> findByCategory (String category, Pageable pageable);
}
