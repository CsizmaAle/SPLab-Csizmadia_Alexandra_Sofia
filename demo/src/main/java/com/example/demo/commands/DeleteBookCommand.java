package com.example.demo.commands;

import com.example.demo.models.Book;
import com.example.demo.persistence.CrudRepository;

public class DeleteBookCommand implements Command<Boolean> {
    private final CrudRepository<Book, Integer> booksRepository;
    private final Integer id;

    public DeleteBookCommand(CrudRepository<Book, Integer> booksRepository, Integer id) {
        this.booksRepository = booksRepository;
        this.id = id;
    }

    @Override
    public Boolean execute() {
        if (!booksRepository.existsById(id)) {
            return false;
        }
        booksRepository.deleteById(id);
        return true;
    }
}
