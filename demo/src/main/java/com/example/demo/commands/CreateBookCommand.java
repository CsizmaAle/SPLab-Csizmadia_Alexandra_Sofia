package com.example.demo.commands;

import com.example.demo.models.BookDto;
import com.example.demo.services.BooksService;
import com.example.demo.services.IsbnService;

// Orchestreaza doua servicii: obtine ISBN-ul, apoi salveaza cartea
public class CreateBookCommand implements Command<BookDto> {
    private final BooksService booksService;
    private final IsbnService isbnService;
    private final BookDto book;

    public CreateBookCommand(BooksService booksService, IsbnService isbnService, BookDto book) {
        this.booksService = booksService;
        this.isbnService = isbnService;
        this.book = book;
    }

    @Override
    public BookDto execute() {
        String isbn = isbnService.generateIsbn();
        return booksService.create(book, isbn);
    }
}
