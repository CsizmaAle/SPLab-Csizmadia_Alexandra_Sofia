package com.example.demo.controllers;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.commands.CommandExecutor;
import com.example.demo.commands.CreateBookCommand;
import com.example.demo.commands.DeleteBookCommand;
import com.example.demo.commands.GetAllBooksCommand;
import com.example.demo.commands.GetBookByIdCommand;
import com.example.demo.commands.RequestTracker;
import com.example.demo.commands.UpdateBookCommand;
import com.example.demo.models.Book;
import com.example.demo.models.BookRequest;
import com.example.demo.models.RequestStatus;
import com.example.demo.persistence.AuthorsCrudRepository;
import com.example.demo.persistence.CrudRepository;
import com.example.demo.services.IsbnService;

@RestController
@RequestMapping("/books")
public class BooksController {
    private final CrudRepository<Book, Integer> booksRepository;
    private final AuthorsCrudRepository authorsRepository;
    private final IsbnService isbnService;
    private final CommandExecutor syncExecutor;
    private final CommandExecutor asyncExecutor;
    private final RequestTracker requestTracker;

    public BooksController(CrudRepository<Book, Integer> booksRepository, AuthorsCrudRepository authorsRepository,
            IsbnService isbnService,
            @Qualifier("syncCommandExecutor") CommandExecutor syncExecutor,
            @Qualifier("asyncCommandExecutor") CommandExecutor asyncExecutor,
            RequestTracker requestTracker) {
        this.booksRepository = booksRepository;
        this.authorsRepository = authorsRepository;
        this.isbnService = isbnService;
        this.syncExecutor = syncExecutor;
        this.asyncExecutor = asyncExecutor;
        this.requestTracker = requestTracker;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return syncExecutor.execute(new GetAllBooksCommand(booksRepository)).join();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        return syncExecutor.execute(new GetBookByIdCommand(booksRepository, id)).join()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RequestStatus> createBook(@RequestBody BookRequest request) {
        CompletableFuture<Book> future = asyncExecutor.execute(
                new CreateBookCommand(booksRepository, authorsRepository, isbnService, request));
        String requestId = requestTracker.track(future);
        return ResponseEntity.accepted()
                .location(URI.create("/books/requests/" + requestId))
                .body(new RequestStatus(requestId, "PENDING", null, null));
    }

    @GetMapping("/requests/{requestId}")
    public ResponseEntity<RequestStatus> getRequestStatus(@PathVariable String requestId) {
        return requestTracker.getStatus(requestId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Integer id, @RequestBody BookRequest request) {
        return syncExecutor.execute(new UpdateBookCommand(booksRepository, authorsRepository, id, request)).join()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        boolean deleted = syncExecutor.execute(new DeleteBookCommand(booksRepository, id)).join();
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
