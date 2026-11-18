package com.example.demo.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.models.Book;

// Adapter: expune repository-ul Spring Data JPA prin interfata noastra CrudRepository
@Component
public class BooksRepositoryAdapter implements CrudRepository<Book, Integer> {
    private final BooksRepository booksRepository;

    public BooksRepositoryAdapter(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    @Override
    public Optional<Book> findById(Integer id) {
        return booksRepository.findById(id);
    }

    @Override
    public Book save(Book book) {
        return booksRepository.save(book);
    }

    @Override
    public void deleteById(Integer id) {
        booksRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return booksRepository.existsById(id);
    }

    @Override
    public long count() {
        return booksRepository.count();
    }
}
