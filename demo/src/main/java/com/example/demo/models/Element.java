package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

// "type" in JSON arata ce fel de element e (Section, Paragraph, ...)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface Element {
    void add(Element element);

    void remove(Element element);

    Element get(int index);

    void accept(Visitor v);
}
