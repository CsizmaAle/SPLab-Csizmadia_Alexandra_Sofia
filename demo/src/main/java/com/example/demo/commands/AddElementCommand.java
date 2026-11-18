package com.example.demo.commands;

import java.util.Optional;

import com.example.demo.models.Book;
import com.example.demo.models.Element;
import com.example.demo.models.ElementRequest;
import com.example.demo.models.ImageProxy;
import com.example.demo.models.Paragraph;
import com.example.demo.models.Section;
import com.example.demo.models.Table;
import com.example.demo.models.TableOfContents;
import com.example.demo.persistence.CrudRepository;

// Adauga un element nou in carte sau intr-o sectiune a ei
// Rezultat gol = cartea sau sectiunea parinte nu exista
public class AddElementCommand implements Command<Optional<Book>> {
    private final CrudRepository<Book, Integer> booksRepository;
    private final Integer bookId;
    private final ElementRequest request;

    public AddElementCommand(CrudRepository<Book, Integer> booksRepository, Integer bookId, ElementRequest request) {
        this.booksRepository = booksRepository;
        this.bookId = bookId;
        this.request = request;
    }

    @Override
    public Optional<Book> execute() {
        // validam inainte de a cauta cartea, ca un tip gresit sa dea 400, nu 404
        Element element = createElement(request);
        return booksRepository.findById(bookId).flatMap(book -> {
            Integer parentId = request.parentId();
            Optional<Section> parent = (parentId == null || parentId.equals(book.getId()))
                    ? Optional.of(book)
                    : ElementTree.findSection(book, parentId);
            return parent.map(section -> {
                section.add(element);
                return booksRepository.save(book);
            });
        });
    }

    private static Element createElement(ElementRequest request) {
        String text = request.text() == null ? "" : request.text().trim();
        if (request.type() == null) {
            throw new IllegalArgumentException("Lipseste tipul elementului");
        }
        return switch (request.type()) {
            case "Section" -> new Section(text);
            case "Paragraph" -> new Paragraph(text);
            // ImageProxy: imaginea reala (lenta) se incarca doar cand e nevoie de continut
            case "Image", "ImageProxy" -> new ImageProxy(text);
            case "Table" -> new Table(text);
            case "TableOfContents" -> new TableOfContents(text.isEmpty() ? "Cuprins" : text);
            default -> throw new IllegalArgumentException("Tip de element necunoscut: " + request.type());
        };
    }
}
