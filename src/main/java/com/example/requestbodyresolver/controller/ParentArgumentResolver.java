package com.example.requestbodyresolver.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class ParentArgumentResolver implements HandlerMethodArgumentResolver {
    private DomainClassConverter<FormattingConversionService> domainClassConverter;

    public ParentArgumentResolver(DomainClassConverter domainClassConverter) {
        this.domainClassConverter = domainClassConverter;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(Reference.class) ;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        log.info("In argument resolver");

        log.info(methodParameter.getParameterName());
        Reference reference = methodParameter.getParameterAnnotation(Reference.class);

        // Require to read input again to parse and obtain reference id
        HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(CachedBodyHttpServletRequest.class);
        String jsonPayload = StreamUtils.copyToString(httpServletRequest.getInputStream(), StandardCharsets.UTF_8);

        DocumentContext parse = JsonPath.parse(jsonPayload);

        String referenceField = reference.idField();
        String referenceId = parse.read(referenceField);

        Class<?> clazz = methodParameter.getParameterType();
        Object foundReference = domainClassConverter.convert(referenceId, TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(clazz));

        if(foundReference == null){
            throw new NoParentException();
        }

        return foundReference;
    }
}
