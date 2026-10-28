package com.example.demo.models;

import jakarta.persistence.Entity;

// "Table" e cuvant rezervat in SQL, asa ca tabelul primeste alt nume
@Entity
@jakarta.persistence.Table(name = "TableElement")
public class Table extends BaseElement {
    private String title;

    protected Table() {
    }

    public Table(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
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
