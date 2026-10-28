package com.example.demo.persistence;

import java.util.List;
import java.util.Optional;

// Operatiile de care au nevoie straturile de sus, independent de JPA
public interface CrudRepository<T, ID> {
    List<T> findAll();

    Optional<T> findById(ID id);

    T save(T entity);

    void deleteById(ID id);

    boolean existsById(ID id);

    long count();
}
