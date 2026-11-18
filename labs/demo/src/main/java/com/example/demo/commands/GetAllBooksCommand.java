package com.example.demo.commands;

import java.util.List;

import com.example.demo.models.Book;
import com.example.demo.persistence.CrudRepository;

public class GetAllBooksCommand implements Command<List<Book>> {
    private final CrudRepository<Book, Integer> booksRepository;

    public GetAllBooksCommand(CrudRepository<Book, Integer> booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public List<Book> execute() {
        return booksRepository.findAll();
    }
}
