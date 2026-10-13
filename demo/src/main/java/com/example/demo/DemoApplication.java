package com.example.demo;

import classses.Author;
import classses.Book;
import classses.BookStatistics;
import classses.GenerateToC;
import classses.Image;
import classses.ImageProxy;
import classses.Paragraph;
import classses.RenderContentVisitor;
import classses.Section;
import classses.Table;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws Exception {
        Section cap1 = new Section("Capitolul 1");
        Paragraph p1 = new Paragraph("Paragraph 1");
        cap1.add(p1);

        Paragraph p2 = new Paragraph("Paragraph 2");
        cap1.add(p2);

        Paragraph p3 = new Paragraph("Paragraph 3");
        cap1.add(p3);

        Paragraph p4 = new Paragraph("Paragraph 4");
        cap1.add(p4);

        cap1.add(new ImageProxy("ImageOne"));
        cap1.add(new Image("ImageTwo"));
        cap1.add(new Paragraph("Some text"));
        cap1.add(new Table("Table 1"));

        BookStatistics stats = new BookStatistics();
        cap1.accept(stats);
        stats.printStatistics();


        // test pentru table of contents si print-ul refacut
        Section cap2 = new Section("Capitolul 2");
        cap2.add(new Paragraph("Paragraph 5"));
        cap2.add(new Table("Table 2"));

        Section cap3 = new Section("Capitolul 3");
        cap3.add(new Paragraph("Paragraph 6"));

        Book book = new Book("Carte");
        book.addAuthor(new Author("Autor"));
        book.add(cap1);
        book.add(cap2);
        book.add(cap3);

        GenerateToC generateToC = new GenerateToC();
        book.accept(generateToC);

        RenderContentVisitor render = new RenderContentVisitor();
        System.out.println();
        generateToC.getToC().accept(render);

        System.out.println();
        book.accept(render);
    }

}
