package com.example.demo.commands;

import java.util.List;

import com.example.demo.models.BookDto;
import com.example.demo.services.BooksService;

public class GetAllBooksCommand implements Command<List<BookDto>> {
    private final BooksService booksService;

    public GetAllBooksCommand(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public List<BookDto> execute() {
        return booksService.findAll();
    }
}
