package com.example.requestbodyresolver.repo;

import com.example.requestbodyresolver.domain.Child;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChildRepository extends MongoRepository<Child, String> {
}
