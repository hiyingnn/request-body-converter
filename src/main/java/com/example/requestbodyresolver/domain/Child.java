package com.example.requestbodyresolver.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("child")
public record Child(@Id String id, String name, Parent parent) {
}
