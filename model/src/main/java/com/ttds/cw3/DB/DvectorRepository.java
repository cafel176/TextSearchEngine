package com.ttds.cw3.DB;

import com.ttds.cw3.Data.DocVector;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DvectorRepository extends MongoRepository<DocVector, String>
{

}
