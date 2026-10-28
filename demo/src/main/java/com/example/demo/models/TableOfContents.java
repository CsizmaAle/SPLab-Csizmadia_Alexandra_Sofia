package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

@Entity
public class TableOfContents extends BaseElement {
    private String title;

    // intrarile se recalculeaza cu GenerateToC, nu se salveaza
    @Transient
    private List<String> chapters = new ArrayList<>();

    @Transient
    private List<Integer> pages = new ArrayList<>();

    protected TableOfContents() {
    }

    public TableOfContents(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void addEntry(String chapter, int page) {
        chapters.add(chapter);
        pages.add(page);
    }

    public List<String> getChapters() {
        return chapters;
    }

    public List<Integer> getPages() {
        return pages;
    }

    @Override
    public void add(Element element) {
    }

    @Override
    public void remove(Element element) {
    }

    @Override
    public Element get(int index) {
        return null;
    }


    @Override
    public void accept(Visitor v){
        v.visit(this);
    }


}
