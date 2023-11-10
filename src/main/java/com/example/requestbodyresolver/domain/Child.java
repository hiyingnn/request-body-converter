package com.example.requestbodyresolver.domain;

import com.example.requestbodyresolver.controller.ParentJacksonConverter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("child")
public record Child(@Id String id,
                    String name,
                    @JsonDeserialize(converter = ParentJacksonConverter.class)
                    Parent parent) {
}
