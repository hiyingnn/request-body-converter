package com.example.requestbodyresolver.repo;

import com.example.requestbodyresolver.domain.Parent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParentRepository extends MongoRepository<Parent, String> {
}
