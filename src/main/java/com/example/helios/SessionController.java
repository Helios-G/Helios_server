package com.mysite.sbb;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionEntityRepository sessionRepository;

    public SessionController(SessionEntityRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @PostMapping
    public SessionEntity create(@RequestBody SessionEntity session) {
        return sessionRepository.save(session);
    }

    @GetMapping
    public List<SessionEntity> findAll() {
        return sessionRepository.findAll();
    }

    @GetMapping("/{id}")
    public SessionEntity findOne(@PathVariable Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    @PutMapping("/{id}")
    public SessionEntity update(@PathVariable Long id, @RequestBody SessionEntity session) {
        session.setSessionId(id);          // 엔티티에 setter 필요
        return sessionRepository.save(session);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sessionRepository.deleteById(id);
    }
}