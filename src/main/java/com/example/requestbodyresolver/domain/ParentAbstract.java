package com.example.requestbodyresolver.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("parent")
@Getter
public abstract class ParentAbstract {
    @Id
    String id;
    String name;
}
