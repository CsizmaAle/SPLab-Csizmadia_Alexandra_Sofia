package com.example.demo.models;

public interface Visitor {
    public void visit(Book b);

    public void visit(Section s);

    public void visit(Image p);

    public void visit(Paragraph p);

    public void visit(Table t);

    public void visit(ImageProxy p);

    public void visit(TableOfContents t);
}
