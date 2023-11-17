package com.example.requestbodyresolver.controller;

import com.example.requestbodyresolver.domain.Parent;
import com.example.requestbodyresolver.repo.ParentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Arrays;
import java.util.Optional;


@Aspect
//@Component
@Slf4j
@RequiredArgsConstructor
public class AspectInjectParent {

  private final ParentRepository parentRepository;

  @Pointcut("execution(public com.example.requestbodyresolver.domain.Child com.example.requestbodyresolver.controller.ChildController.*(..))")
  public void injectWithParentPointcut(){}

  @Around("injectWithParentPointcut()")
  public void injectWithParent(final ProceedingJoinPoint pjp) throws Throwable {
    log.info("In Aspect from execution");
    var reference = RequestContextHolder.getRequestAttributes().getAttribute("parent", RequestAttributes.SCOPE_REQUEST);
    Object[] objects = pjp.getArgs();
    log.info(Arrays.toString(objects));
    for(int idx =0; idx < objects.length; idx++) {
      Object o = objects[idx];
      if (o instanceof Parent) {
        // due to erasure of Optional, can't use this
        objects[idx] = new Parent("sad", "alalla");
      }
    }
    pjp.proceed(objects);

  }
}
