package com.example.requestbodyresolver.repo;

import com.example.requestbodyresolver.domain.ParentAbstract;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParentRepository extends MongoRepository<ParentAbstract, String> {
}
