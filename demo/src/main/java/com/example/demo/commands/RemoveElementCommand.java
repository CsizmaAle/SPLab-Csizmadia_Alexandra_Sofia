package com.example.demo.commands;

import java.util.Optional;

import com.example.demo.models.Book;
import com.example.demo.persistence.CrudRepository;

// Sterge un element (si tot ce contine, daca e sectiune) din carte
// Rezultat gol = cartea sau elementul nu exista
public class RemoveElementCommand implements Command<Optional<Book>> {
    private final CrudRepository<Book, Integer> booksRepository;
    private final Integer bookId;
    private final Integer elementId;

    public RemoveElementCommand(CrudRepository<Book, Integer> booksRepository, Integer bookId, Integer elementId) {
        this.booksRepository = booksRepository;
        this.bookId = bookId;
        this.elementId = elementId;
    }

    @Override
    public Optional<Book> execute() {
        return booksRepository.findById(bookId).flatMap(book -> {
            if (!ElementTree.remove(book, elementId)) {
                return Optional.empty();
            }
            return Optional.of(booksRepository.save(book));
        });
    }
}
