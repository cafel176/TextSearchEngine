package com.ttds.cw3.DB;

import com.ttds.cw3.Data.DocVector;
import com.ttds.cw3.Data.TermVector;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TvectorRepository extends MongoRepository<TermVector, String>
{
}
