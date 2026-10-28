package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.demo.models.BookDto;

@Service
public class BooksService {
    private final Map<Long, BookDto> books = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<BookDto> findAll() {
        return new ArrayList<>(books.values());
    }

    public Optional<BookDto> findById(Long id) {
        return Optional.ofNullable(books.get(id));
    }

    public BookDto create(BookDto book, String isbn) {
        Long id = nextId.getAndIncrement();
        BookDto saved = new BookDto(id, book.title(), book.authors(), isbn);
        books.put(id, saved);
        return saved;
    }

    public Optional<BookDto> update(Long id, BookDto book) {
        BookDto existing = books.get(id);
        if (existing == null) {
            return Optional.empty();
        }
        BookDto updated = new BookDto(id, book.title(), book.authors(), existing.isbn());
        books.put(id, updated);
        return Optional.of(updated);
    }

    public boolean delete(Long id) {
        return books.remove(id) != null;
    }
}
