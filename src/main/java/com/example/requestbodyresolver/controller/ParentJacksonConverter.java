package com.example.requestbodyresolver.controller;

import com.example.requestbodyresolver.domain.Parent;
import com.example.requestbodyresolver.repo.ParentRepository;
import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParentJacksonConverter extends StdConverter<String, Parent> {

  private final ParentRepository parentRepository;

  @Override
  public Parent convert(String id) {
    return parentRepository.findById(id).orElseThrow(NoParentException::new);
  }
}


