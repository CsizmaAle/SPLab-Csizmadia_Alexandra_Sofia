package com.example.demo.observers;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.models.Book;

// ConcreteObserver: trimite cartea ca JSON catre browser prin Server-Sent Events
public class SseObserver implements Observer {
    private final SseEmitter emitter;

    public SseObserver(SseEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void update(Book book) {
        try {
            emitter.send(book, MediaType.APPLICATION_JSON);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
