package com.example.requestbodyresolver.controller;

import com.example.requestbodyresolver.domain.Parent;
import com.example.requestbodyresolver.repo.ParentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

//@Component
@RequiredArgsConstructor
@Slf4j
public class ParentArgumentResolver implements HandlerMethodArgumentResolver {
    private final ParentRepository parentRepository;
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(Parent.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        log.info("In argument resolver");
        var reference = (String) RequestContextHolder.getRequestAttributes().getAttribute("parentId", RequestAttributes.SCOPE_REQUEST);
        return parentRepository.findById(reference).get();
    }
}
