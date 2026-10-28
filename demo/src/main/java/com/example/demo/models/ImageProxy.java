package com.example.demo.models;

import java.awt.Dimension;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

@Entity
public class ImageProxy extends BaseElement implements Picture {
    private String url;

    @Transient
    private Dimension dim;

    @Transient
    private Image realImg;

    protected ImageProxy() {
    }

    public ImageProxy(String url) {
        this.url = url;
    }

    private Image loadImage() {
        if (realImg == null) {
            realImg = new Image(url);
        }
        return realImg;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public Dimension dim() {
        return dim;
    }

    @Override
    public PictureContent content() {
        return loadImage().content();
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
