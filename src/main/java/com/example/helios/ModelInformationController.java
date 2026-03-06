package com.mysite.sbb;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/model-informations")
public class ModelInformationController {

    private final ModelInformationRepository repository;

    public ModelInformationController(ModelInformationRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ModelInformation create(@RequestBody ModelInformation info) {
        return repository.save(info);
    }

    @GetMapping
    public List<ModelInformation> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ModelInformation findOne(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ModelInformation not found"));
    }

    @PutMapping("/{id}")
    public ModelInformation update(@PathVariable Long id, @RequestBody ModelInformation info) {
        info.setModelInformationId(id);
        return repository.save(info);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}