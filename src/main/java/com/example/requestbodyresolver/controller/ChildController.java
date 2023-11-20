package com.example.requestbodyresolver.controller;

import com.example.requestbodyresolver.domain.Child;
import com.example.requestbodyresolver.domain.Parent;
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

    @GetMapping("child/{child}")
    public Optional<Child> getChildById(Child child, @PathVariable String childId) {
        log.info(child.toString());
        return childRepository.findById(childId);
    }

    @PostMapping("child")
    public Child createChild(
            @RequestBody
            Child child,
            @Reference(
            idField = "parentId"
    ) Parent parent) {
      Child createdChild = childRepository.save(child);
      log.info("Parent found {}", parent);
      log.info("Child created {}", createdChild);
      return createdChild;
    }
}
