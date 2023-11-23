package com.example.requestbodyresolver.controller;

import com.example.requestbodyresolver.domain.ParentAbstract;
import com.example.requestbodyresolver.domain.ParentConcrete;
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
  public List<ParentAbstract> getAllParents() {
    return parentRepository.findAll();
  }

  @GetMapping("parent/{parentId}")
  public Optional<ParentAbstract> getParentById(@PathVariable String parentId) {
    return parentRepository.findById(parentId);
  }

  @PostMapping("parent")
  public ParentAbstract createParent(@RequestBody ParentConcrete parent) {
    return parentRepository.save(parent);
  }
}
