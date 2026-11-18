package com.example.demo.commands;

import java.util.Optional;

import com.example.demo.models.Book;
import com.example.demo.persistence.CrudRepository;

public class GetBookByIdCommand implements Command<Optional<Book>> {
    private final CrudRepository<Book, Integer> booksRepository;
    private final Integer id;

    public GetBookByIdCommand(CrudRepository<Book, Integer> booksRepository, Integer id) {
        this.booksRepository = booksRepository;
        this.id = id;
    }

    @Override
    public Optional<Book> execute() {
        return booksRepository.findById(id);
    }
}
