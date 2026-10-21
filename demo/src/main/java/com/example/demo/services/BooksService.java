package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.demo.models.Book;

@Service
public class BooksService {
    private final Map<Long, Book> books = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(books.get(id));
    }

    public Book create(Book book, String isbn) {
        Long id = nextId.getAndIncrement();
        Book saved = new Book(id, book.title(), book.authors(), isbn);
        books.put(id, saved);
        return saved;
    }

    public Optional<Book> update(Long id, Book book) {
        Book existing = books.get(id);
        if (existing == null) {
            return Optional.empty();
        }
        Book updated = new Book(id, book.title(), book.authors(), existing.isbn());
        books.put(id, updated);
        return Optional.of(updated);
    }

    public boolean delete(Long id) {
        return books.remove(id) != null;
    }
}
