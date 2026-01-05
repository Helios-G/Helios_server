package com.mysite.sbb;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work-codes")
public class WorkCodeController {

    private final WorkCodeRepository repository;

    public WorkCodeController(WorkCodeRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public WorkCode create(@RequestBody WorkCode workCode) {
        return repository.save(workCode);
    }

    @GetMapping
    public List<WorkCode> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public WorkCode findOne(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkCode not found"));
    }

    @PutMapping("/{id}")
    public WorkCode update(@PathVariable Long id, @RequestBody WorkCode workCode) {
        workCode.setWorkCodeId(id);
        return repository.save(workCode);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}