package com.example.requestbodyresolver.controller;

import com.example.requestbodyresolver.domain.Child;
import com.example.requestbodyresolver.domain.Parent;
import com.example.requestbodyresolver.repo.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@ControllerAdvice
public class ParentRequestBodyAdvice extends RequestBodyAdviceAdapter {

  private final ParentRepository parentRepository;

  @Override
  public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
    return Objects.equals(targetType.getTypeName(), Child.class.getTypeName());
  }

  @Override
  public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
    Child child = (Child) body;
    // given that we aren't able to deserialize into a String, we need to
    Parent paregit ntId = child.parent();
    Optional<Parent> parent = parentRepository.findById(parentId.id());
    if(parent.isEmpty()) {
      throw new NoParentException();
    }
    Child updatedChild = child.toBuilder()
      .parent(parent.get())
      .build();
    return updatedChild;
  }
}
