package com.example.demo.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BooksController {

    @GetMapping
    public List<String> getAllBooks() {
        return List.of("Book 1", "Book 2");
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable Long id) {
        return "Book with id " + id;
    }

    @PostMapping
    public String createBook(@RequestBody String book) {
        return "Created book: " + book;
    }

    @PutMapping("/{id}")
    public String updateBook(@PathVariable Long id, @RequestBody String book) {
        return "Updated book with id " + id + ": " + book;
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        return "Deleted book with id " + id;
    }
}
