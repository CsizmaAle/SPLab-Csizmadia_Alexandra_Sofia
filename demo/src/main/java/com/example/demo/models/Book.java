package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
public class Book extends Section {
    private String isbn;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Author> authors = new ArrayList<>();

    protected Book() {
    }

    public Book(String title, List<Author> authors, List<Element> element) {
        super(title);
        this.authors.addAll(authors);
        for (Element e : element) {
            add(e);
        }
    }

    public Book(String title) {
        super(title);
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void addContent(Element e) {
        add(e);
    }

    @Override
    public void accept(Visitor v){
        v.visit(this);
        for (Element child : getChildren()) {
            child.accept(v);
        }
    }
}
