package com.example.demo.commands;

import com.example.demo.models.Book;
import com.example.demo.models.BookRequest;
import com.example.demo.persistence.AuthorsCrudRepository;
import com.example.demo.persistence.CrudRepository;
import com.example.demo.services.IsbnService;

// Orchestreaza mai multe servicii: obtine ISBN-ul, autorii, apoi salveaza cartea
public class CreateBookCommand implements Command<Book> {
    private final CrudRepository<Book, Integer> booksRepository;
    private final AuthorsCrudRepository authorsRepository;
    private final IsbnService isbnService;
    private final BookRequest request;

    public CreateBookCommand(CrudRepository<Book, Integer> booksRepository, AuthorsCrudRepository authorsRepository,
            IsbnService isbnService, BookRequest request) {
        this.booksRepository = booksRepository;
        this.authorsRepository = authorsRepository;
        this.isbnService = isbnService;
        this.request = request;
    }

    @Override
    public Book execute() {
        Book book = new Book(request.title());
        book.setIsbn(isbnService.generateIsbn());
        for (String name : request.authors()) {
            book.addAuthor(authorsRepository.findOrCreate(name));
        }
        return booksRepository.save(book);
    }
}
