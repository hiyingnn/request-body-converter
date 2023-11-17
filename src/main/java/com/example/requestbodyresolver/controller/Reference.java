package com.example.requestbodyresolver.controller;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Reference {
    String[] idField() default {};
}
