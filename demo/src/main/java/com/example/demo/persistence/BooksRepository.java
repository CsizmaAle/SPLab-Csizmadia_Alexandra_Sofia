package com.example.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Book;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
}
