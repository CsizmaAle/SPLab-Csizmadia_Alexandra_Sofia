package com.example.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

@Entity
public class Paragraph extends BaseElement {
    private String text;

    @Transient
    private AlignStrategy textAlignment;

    protected Paragraph() {
    }

    public Paragraph(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setAlignStrategy(AlignStrategy textAlignment) {
        this.textAlignment = textAlignment;
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

    public AlignStrategy getTextAlignment(){
        return textAlignment;
    }


    @Override 
    public void accept(Visitor v){
        v.visit(this);
    }

}
