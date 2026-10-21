package com.example.demo.commands;

import java.util.List;

import com.example.demo.models.Book;
import com.example.demo.services.BooksService;

public class GetAllBooksCommand implements Command<List<Book>> {
    private final BooksService booksService;

    public GetAllBooksCommand(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public List<Book> execute() {
        return booksService.findAll();
    }
}
