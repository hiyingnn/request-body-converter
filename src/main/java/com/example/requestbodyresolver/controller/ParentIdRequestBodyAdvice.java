package com.example.requestbodyresolver.controller;

import com.example.requestbodyresolver.domain.Child;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.Objects;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ParentIdRequestBodyAdvice extends RequestBodyAdviceAdapter {

  private final ObjectMapper objectMapper;
  @Override
  public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
    return Objects.equals(targetType.getTypeName(), Child.class.getTypeName()) && methodParameter.hasParameterAnnotation(Reference.class) ;
  }

  @SneakyThrows
  @Override
  public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
    log.info(parameter.getParameterName());
    Reference reference = parameter.getParameterAnnotation(Reference.class);
    log.info("In Request Body Adapter");

    String valueStr = objectMapper.writeValueAsString(body);

    DocumentContext parse = JsonPath.parse(valueStr);

    String[] referenceFields = reference.idField();
    for (String referenceField: referenceFields){
      Object read = parse.read(referenceField);
      RequestContextHolder.getRequestAttributes().setAttribute(referenceField, read, RequestAttributes.SCOPE_REQUEST);
    }

    return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
  }
}
