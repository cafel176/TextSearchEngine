package com.ttds.cw3.DB;

import com.ttds.cw3.Data.Doc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocRepository extends MongoRepository<Doc,String>
{
    List<Doc> findByAuthor (String author);
    List<Doc> findByCategory (String category);
}
