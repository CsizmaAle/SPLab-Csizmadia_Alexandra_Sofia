package com.example.demo.commands;

import com.example.demo.models.Book;
import com.example.demo.services.BooksService;
import com.example.demo.services.IsbnService;

// Orchestreaza doua servicii: obtine ISBN-ul, apoi salveaza cartea
public class CreateBookCommand implements Command<Book> {
    private final BooksService booksService;
    private final IsbnService isbnService;
    private final Book book;

    public CreateBookCommand(BooksService booksService, IsbnService isbnService, Book book) {
        this.booksService = booksService;
        this.isbnService = isbnService;
        this.book = book;
    }

    @Override
    public Book execute() {
        String isbn = isbnService.generateIsbn();
        return booksService.create(book, isbn);
    }
}
