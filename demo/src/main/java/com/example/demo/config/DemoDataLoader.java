package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.models.Book;
import com.example.demo.models.ImageProxy;
import com.example.demo.models.Paragraph;
import com.example.demo.models.Section;
import com.example.demo.models.Table;
import com.example.demo.models.TableOfContents;
import com.example.demo.persistence.AuthorsCrudRepository;
import com.example.demo.persistence.CrudRepository;

// Salveaza o carte demo la prima pornire, ca sa se vada structura in baza de date
@Component
public class DemoDataLoader implements CommandLineRunner {
    private final CrudRepository<Book, Integer> booksRepository;
    private final AuthorsCrudRepository authorsRepository;

    public DemoDataLoader(CrudRepository<Book, Integer> booksRepository, AuthorsCrudRepository authorsRepository) {
        this.booksRepository = booksRepository;
        this.authorsRepository = authorsRepository;
    }

    @Override
    public void run(String... args) {
        if (booksRepository.count() > 0) {
            return;
        }

        Book book = new Book("Carte demo");
        book.setIsbn("978-0000000000");
        book.addAuthor(authorsRepository.findOrCreate("Ana Popescu"));
        book.addAuthor(authorsRepository.findOrCreate("Ion Ionescu"));

        book.add(new TableOfContents("Cuprins"));

        Section cap1 = new Section("Capitolul 1");
        cap1.add(new Paragraph("Paragraph 1"));
        cap1.add(new Paragraph("Paragraph 2"));
        cap1.add(new ImageProxy("ImageOne"));
        cap1.add(new Table("Table 1"));
        book.add(cap1);

        Section cap2 = new Section("Capitolul 2");
        cap2.add(new Paragraph("Paragraph 3"));
        Section sub = new Section("Subcapitolul 2.1");
        sub.add(new Paragraph("Paragraph 4"));
        cap2.add(sub);
        book.add(cap2);

        booksRepository.save(book);
    }
}
