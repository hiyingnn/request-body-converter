package com.example.requestbodyresolver.domain;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("child")
@Builder(toBuilder = true)
public record Child(@Id String id,
                    String name,
//                    @JsonDeserialize(converter = ParentJacksonConverter.class)
                    Parent parent) {
}
