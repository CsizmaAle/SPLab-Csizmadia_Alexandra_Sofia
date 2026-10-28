package com.example.demo.models;

import java.awt.Dimension;
import java.util.concurrent.TimeUnit;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

@Entity
public class Image extends BaseElement implements Picture {
    private String url;

    @Transient
    private ImageContent content;

    protected Image() {
    }

    public Image(String url) {
        this.url = url;
        this.content = new ImageContent();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public Dimension dim() {
        return null;
    }

    @Override
    public ImageContent content() {
        return content;
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
