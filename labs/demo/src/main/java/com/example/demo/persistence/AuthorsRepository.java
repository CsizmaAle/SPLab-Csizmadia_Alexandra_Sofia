package com.example.demo.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Author;

@Repository
public interface AuthorsRepository extends JpaRepository<Author, Integer> {

    Optional<Author> findFirstByName(String name);
}
