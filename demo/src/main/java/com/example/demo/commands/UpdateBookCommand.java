package com.example.demo.commands;

import java.util.Optional;

import com.example.demo.models.BookDto;
import com.example.demo.services.BooksService;

public class UpdateBookCommand implements Command<Optional<BookDto>> {
    private final BooksService booksService;
    private final Long id;
    private final BookDto book;

    public UpdateBookCommand(BooksService booksService, Long id, BookDto book) {
        this.booksService = booksService;
        this.id = id;
        this.book = book;
    }

    @Override
    public Optional<BookDto> execute() {
        return booksService.update(id, book);
    }
}
