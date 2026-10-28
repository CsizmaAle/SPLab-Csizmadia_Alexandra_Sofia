package com.example.demo.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.models.Author;

// Adapter: expune repository-ul Spring Data JPA prin interfata noastra AuthorsCrudRepository
@Component
public class AuthorsRepositoryAdapter implements AuthorsCrudRepository {
    private final AuthorsRepository authorsRepository;

    public AuthorsRepositoryAdapter(AuthorsRepository authorsRepository) {
        this.authorsRepository = authorsRepository;
    }

    @Override
    public List<Author> findAll() {
        return authorsRepository.findAll();
    }

    @Override
    public Optional<Author> findById(Integer id) {
        return authorsRepository.findById(id);
    }

    @Override
    public Author save(Author author) {
        return authorsRepository.save(author);
    }

    @Override
    public void deleteById(Integer id) {
        authorsRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return authorsRepository.existsById(id);
    }

    @Override
    public long count() {
        return authorsRepository.count();
    }

    @Override
    public Author findOrCreate(String name) {
        return authorsRepository.findFirstByName(name)
                .orElseGet(() -> authorsRepository.save(new Author(name)));
    }
}
