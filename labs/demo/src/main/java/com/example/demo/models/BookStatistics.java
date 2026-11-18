package com.example.demo.models;

public class BookStatistics implements Visitor{
    
    private int books = 0;
    private int sections = 0;
    private int paragraphs = 0;
    private int tablesOfContents = 0;
    private int images = 0;
    private int tables = 0;

    @Override
    public void visit(Book book) {
        books++;
    }

    @Override
    public void visit(Section section) {
        sections++;
    }

    @Override
    public void visit(TableOfContents toc) {
        tablesOfContents++;
    }

    @Override
    public void visit(Paragraph paragraph) {
        paragraphs++;
    }

    @Override
    public void visit(ImageProxy imageProxy) {
        images++;
    }

    @Override
    public void visit(Table table) {
        tables++;
    }

    @Override 
    public void visit(Image im){
        images ++;
    }

    public void printStatistics() {
        System.out.println("Book statistics:");
        System.out.println("*** Number of sections: " + sections);
        System.out.println("*** Number of paragraphs: " + paragraphs);
        System.out.println("*** Number of images: " + images);
        System.out.println("*** Number of tables: " + tables);
    }
}
