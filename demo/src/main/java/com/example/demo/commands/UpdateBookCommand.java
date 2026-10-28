package com.example.demo.commands;

import java.util.List;
import java.util.Optional;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.models.BookRequest;
import com.example.demo.persistence.AuthorsCrudRepository;
import com.example.demo.persistence.CrudRepository;

// Schimba doar titlul si autorii; continutul si ISBN-ul raman neschimbate
public class UpdateBookCommand implements Command<Optional<Book>> {
    private final CrudRepository<Book, Integer> booksRepository;
    private final AuthorsCrudRepository authorsRepository;
    private final Integer id;
    private final BookRequest request;

    public UpdateBookCommand(CrudRepository<Book, Integer> booksRepository, AuthorsCrudRepository authorsRepository,
            Integer id, BookRequest request) {
        this.booksRepository = booksRepository;
        this.authorsRepository = authorsRepository;
        this.id = id;
        this.request = request;
    }

    @Override
    public Optional<Book> execute() {
        return booksRepository.findById(id).map(book -> {
            book.setTitle(request.title());
            List<Author> authors = request.authors().stream()
                    .map(authorsRepository::findOrCreate)
                    .toList();
            book.setAuthors(authors);
            return booksRepository.save(book);
        });
    }
}
