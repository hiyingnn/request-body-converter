package com.example.requestbodyresolver.controller;

import com.example.requestbodyresolver.domain.Child;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class ParentIdRequestBodyAdvice extends RequestBodyAdviceAdapter {

  @Override
  public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
    return Objects.equals(targetType.getTypeName(), Child.class.getTypeName());
  }

  @Override
  public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
    log.info(parameter.getParameterName());
    Reference reference = parameter.getParameterAnnotation(Reference.class);
    log.info(reference.idField());
    return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
  }
}