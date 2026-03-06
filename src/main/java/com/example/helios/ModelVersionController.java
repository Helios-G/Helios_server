package com.mysite.sbb;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/model-versions")
public class ModelVersionController {

    private final ModelVersionRepository repository;

    public ModelVersionController(ModelVersionRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ModelVersion create(@RequestBody ModelVersion modelVersion) {
        return repository.save(modelVersion);
    }

    @GetMapping
    public List<ModelVersion> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ModelVersion findOne(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ModelVersion not found"));
    }

    @PutMapping("/{id}")
    public ModelVersion update(@PathVariable Long id, @RequestBody ModelVersion modelVersion) {
        modelVersion.setModelVersionId(id);
        return repository.save(modelVersion);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}