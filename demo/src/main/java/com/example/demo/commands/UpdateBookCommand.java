package com.example.demo.commands;

import java.util.Optional;

import com.example.demo.models.Book;
import com.example.demo.services.BooksService;

public class UpdateBookCommand implements Command<Optional<Book>> {
    private final BooksService booksService;
    private final Long id;
    private final Book book;

    public UpdateBookCommand(BooksService booksService, Long id, Book book) {
        this.booksService = booksService;
        this.id = id;
        this.book = book;
    }

    @Override
    public Optional<Book> execute() {
        return booksService.update(id, book);
    }
}
