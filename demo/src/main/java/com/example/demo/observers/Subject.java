package com.example.demo.observers;

import com.example.demo.models.Book;

public interface Subject {
    void attach(Observer observer);

    void detach(Observer observer);

    void notifyObservers(Book book);
}
