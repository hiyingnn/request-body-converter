package com.example.requestbodyresolver.controller;

import com.example.requestbodyresolver.domain.Parent;
import com.example.requestbodyresolver.repo.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class ParentController {
  private final ParentRepository parentRepository;

  @GetMapping("parent")
  public List<Parent> getAllParents() {
    return parentRepository.findAll();
  }

  @GetMapping("parent/{parentId}")
  public Optional<Parent> getParentById(@PathVariable String parentId) {
    return parentRepository.findById(parentId);
  }

  @PostMapping("parent")
  public Parent createChild(@RequestBody Parent parent) {
    return parentRepository.save(parent);
  }
}
