package com.example.demo.commands;

import java.util.List;

import com.example.demo.models.Book;
import com.example.demo.persistence.BooksRepository;

public class GetAllBooksCommand implements Command<List<Book>> {
    private final BooksRepository booksRepository;

    public GetAllBooksCommand(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public List<Book> execute() {
        return booksRepository.findAll();
    }
}
