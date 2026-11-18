package com.example.demo.config;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.Book;
import com.example.demo.models.Element;
import com.example.demo.models.Section;
import com.example.demo.models.TableOfContents;
import com.example.demo.persistence.CrudRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// La pornire, aduce cartile deja salvate la regula: fiecare carte are exact un cuprins,
// pe prima pozitie. Cartile noi primesc cuprinsul direct la creare (CreateBookCommand).
@Component
public class TableOfContentsInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(TableOfContentsInitializer.class);

    private final CrudRepository<Book, Integer> booksRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TableOfContentsInitializer(CrudRepository<Book, Integer> booksRepository) {
        this.booksRepository = booksRepository;
    }

    // tranzactie, ca sa se poata incarca continutul (lazy) al cartilor
    @Override
    @Transactional
    public void run(String... args) {
        for (Book book : booksRepository.findAll()) {
            TableOfContents toc = firstDirectTableOfContents(book);

            // 1. scoatem cuprinsurile in plus (al doilea, cele din capitole)
            boolean changed = removeOtherTablesOfContents(book, toc) > 0;

            // 2. daca nu are cuprins, il adaugam la final (pasul 3 il muta la inceput)
            if (toc == null) {
                toc = new TableOfContents("Cuprins");
                book.add(toc);
                changed = true;
            }
            booksRepository.save(book);
            entityManager.flush();

            // 3. mutam cuprinsul pe prima pozitie
            int index = book.getChildren().indexOf(toc);
            if (index > 0) {
                moveToFront(book.getId(), index);
                changed = true;
            }

            if (changed) {
                log.info("Cuprins pus la inceputul cartii #{} ({})", book.getId(), book.getTitle());
            }
        }
        // ordinea s-a schimbat direct in baza de date, deci obiectele incarcate nu mai sunt actuale
        entityManager.clear();
    }

    private static TableOfContents firstDirectTableOfContents(Book book) {
        for (Element child : book.getChildren()) {
            if (child instanceof TableOfContents toc) {
                return toc;
            }
        }
        return null;
    }

    private static int removeOtherTablesOfContents(Section section, TableOfContents keep) {
        int removed = 0;
        Iterator<Element> it = section.getChildren().iterator();
        while (it.hasNext()) {
            Element child = it.next();
            if (child instanceof TableOfContents && child != keep) {
                it.remove();
                removed++;
            } else if (child instanceof Section nested) {
                removed += removeOtherTablesOfContents(nested, keep);
            }
        }
        return removed;
    }

    // Mutarea se face in SQL: Hibernate actualizeaza pozitiile listei (@OrderColumn) de la
    // final spre inceput, iar un element nu poate aparea de doua ori in Section_children,
    // asa ca orice deplasare spre dreapta ar incalca constrangerea de unicitate.
    // Aici pozitiile 0..index-1 trec temporar pe valori foarte mari, ca sa nu se suprapuna
    // (valori negative nu se pot folosi: coloana are constrangerea children_ORDER >= 0).
    private static final int TEMP_OFFSET = 1_000_000;

    private void moveToFront(Integer bookId, int index) {
        // 0..index-1  ->  OFFSET+1..OFFSET+index
        entityManager.createNativeQuery(
                "update Section_children set children_ORDER = children_ORDER + ?3"
                        + " where Section_id = ?1 and children_ORDER < ?2")
                .setParameter(1, bookId).setParameter(2, index).setParameter(3, TEMP_OFFSET + 1)
                .executeUpdate();
        // cuprinsul: index  ->  0
        entityManager.createNativeQuery(
                "update Section_children set children_ORDER = 0"
                        + " where Section_id = ?1 and children_ORDER = ?2")
                .setParameter(1, bookId).setParameter(2, index).executeUpdate();
        // OFFSET+1..OFFSET+index  ->  1..index
        entityManager.createNativeQuery(
                "update Section_children set children_ORDER = children_ORDER - ?2"
                        + " where Section_id = ?1 and children_ORDER > ?2")
                .setParameter(1, bookId).setParameter(2, TEMP_OFFSET).executeUpdate();
    }
}
