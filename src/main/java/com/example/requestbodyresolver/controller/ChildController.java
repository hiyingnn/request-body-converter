package com.example.requestbodyresolver.controller;

import com.example.requestbodyresolver.domain.Child;
import com.example.requestbodyresolver.repo.ChildRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class ChildController {
    private final ChildRepository childRepository;

    @GetMapping("child")
    public List<Child> getAllChildren() {
      return childRepository.findAll();
    }

    @GetMapping("child/{childId}")
    public Optional<Child> getChildById(@PathVariable String childId) {
      return childRepository.findById(childId);
    }

    @PostMapping("child")
    public Child createChild(@RequestBody Child child) {
      Child createdChild = childRepository.save(child);
      log.info("Child created {}", createdChild);
      return createdChild;
    }
}
