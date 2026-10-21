package com.example.demo.commands;

import java.util.Optional;

import com.example.demo.models.Book;
import com.example.demo.services.BooksService;

public class GetBookByIdCommand implements Command<Optional<Book>> {
    private final BooksService booksService;
    private final Long id;

    public GetBookByIdCommand(BooksService booksService, Long id) {
        this.booksService = booksService;
        this.id = id;
    }

    @Override
    public Optional<Book> execute() {
        return booksService.findById(id);
    }
}
