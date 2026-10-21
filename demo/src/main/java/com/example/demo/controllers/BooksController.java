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
import com.example.demo.models.RequestStatus;
import com.example.demo.services.BooksService;
import com.example.demo.services.IsbnService;

@RestController
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final IsbnService isbnService;
    private final CommandExecutor syncExecutor;
    private final CommandExecutor asyncExecutor;
    private final RequestTracker requestTracker;

    public BooksController(BooksService booksService, IsbnService isbnService,
            @Qualifier("syncCommandExecutor") CommandExecutor syncExecutor,
            @Qualifier("asyncCommandExecutor") CommandExecutor asyncExecutor,
            RequestTracker requestTracker) {
        this.booksService = booksService;
        this.isbnService = isbnService;
        this.syncExecutor = syncExecutor;
        this.asyncExecutor = asyncExecutor;
        this.requestTracker = requestTracker;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return syncExecutor.execute(new GetAllBooksCommand(booksService)).join();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return syncExecutor.execute(new GetBookByIdCommand(booksService, id)).join()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RequestStatus> createBook(@RequestBody Book book) {
        CompletableFuture<Book> future = asyncExecutor.execute(new CreateBookCommand(booksService, isbnService, book));
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
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return syncExecutor.execute(new UpdateBookCommand(booksService, id, book)).join()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean deleted = syncExecutor.execute(new DeleteBookCommand(booksService, id)).join();
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
