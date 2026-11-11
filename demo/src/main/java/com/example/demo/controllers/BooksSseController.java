package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.models.Book;
import com.example.demo.observers.AllBooksSubject;
import com.example.demo.observers.SseObserver;
import com.example.demo.persistence.CrudRepository;

@RestController
public class BooksSseController {
    private final AllBooksSubject allBooksSubject;
    private final CrudRepository<Book, Integer> booksRepository;

    public BooksSseController(AllBooksSubject allBooksSubject, CrudRepository<Book, Integer> booksRepository) {
        this.allBooksSubject = allBooksSubject;
        this.booksRepository = booksRepository;
    }

    @GetMapping("/books-sse")
    public ResponseBodyEmitter getBooksSse() {
        // 0L = conexiunea nu expira
        final SseEmitter emitter = new SseEmitter(0L);
        SseObserver observer = new SseObserver(emitter);

        // trimite intai cartile existente, apoi asculta cartile noi
        for (Book book : booksRepository.findAll()) {
            observer.update(book);
        }
        allBooksSubject.attach(observer);

        // cand clientul se deconecteaza, observatorul e scos din lista
        emitter.onCompletion(() -> allBooksSubject.detach(observer));
        emitter.onTimeout(() -> allBooksSubject.detach(observer));
        emitter.onError(e -> allBooksSubject.detach(observer));
        return emitter;
    }
}
