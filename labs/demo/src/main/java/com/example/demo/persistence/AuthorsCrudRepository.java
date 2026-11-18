package com.example.demo.persistence;

import com.example.demo.models.Author;

public interface AuthorsCrudRepository extends CrudRepository<Author, Integer> {
    // refoloseste autorul existent cu acelasi nume, altfel il salveaza pe cel nou
    Author findOrCreate(String name);
}
