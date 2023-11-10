package com.example.requestbodyresolver.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("parent")
public record Parent(@Id String id, String name) {
}
