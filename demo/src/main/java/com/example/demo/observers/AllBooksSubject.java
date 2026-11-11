package com.example.demo.observers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import com.example.demo.models.Book;

// ConcreteSubject: anunta observatorii la fiecare carte noua
@Component
public class AllBooksSubject implements Subject {
    // lista sigura pentru acces din mai multe thread-uri (cereri web + thread pool-ul asincron)
    private final List<Observer> observers = new CopyOnWriteArrayList<>();

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Book book) {
        for (Observer observer : observers) {
            try {
                observer.update(book);
            } catch (RuntimeException e) {
                // clientul nu mai asculta (ex. a inchis pagina)
                detach(observer);
            }
        }
    }

    public void add(Book book) {
        notifyObservers(book);
    }
}
