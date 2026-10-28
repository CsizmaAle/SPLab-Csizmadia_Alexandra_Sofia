package com.example.demo.commands;

import com.example.demo.persistence.BooksRepository;

public class DeleteBookCommand implements Command<Boolean> {
    private final BooksRepository booksRepository;
    private final Integer id;

    public DeleteBookCommand(BooksRepository booksRepository, Integer id) {
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
