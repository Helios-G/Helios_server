package com.mysite.sbb;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospital-status-codes")
public class HospitalStatusCodeController {

    private final HospitalStatusCodeRepository repository;

    public HospitalStatusCodeController(HospitalStatusCodeRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public HospitalStatusCode create(@RequestBody HospitalStatusCode code) {
        return repository.save(code);
    }

    @GetMapping
    public List<HospitalStatusCode> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public HospitalStatusCode findOne(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("HospitalStatusCode not found"));
    }

    @PutMapping("/{id}")
    public HospitalStatusCode update(@PathVariable Long id, @RequestBody HospitalStatusCode code) {
        code.setHospitalStatusCodeId(id);
        return repository.save(code);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}