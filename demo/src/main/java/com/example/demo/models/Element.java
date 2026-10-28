package com.example.demo.models;

public interface Element {
    void add(Element element);

    void remove(Element element);

    Element get(int index);

    void accept(Visitor v);
}
