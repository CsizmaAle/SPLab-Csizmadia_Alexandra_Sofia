package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
public class Book extends Section {
    private String isbn;

    // fara cascade: autorii se salveaza separat, ca sa poata fi refolositi intre carti
    @ManyToMany
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

    public void setAuthors(List<Author> authors) {
        this.authors = new ArrayList<>(authors);
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
