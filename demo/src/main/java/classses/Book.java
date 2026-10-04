package classses;
import java.util.List;

import java.util.ArrayList;

public class Book {
    private String title;
    private List<Author> authors = new ArrayList<>();
    private List<Element> element = new ArrayList<>();

    public Book(String title, List<Author> authors, List<Element> element) {
        this.title = title;
        this.authors.addAll(authors);
        this.element.addAll(element);
    }

    public Book(String title) {
        this.title = title;
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }


    public String getTitle() {
        return title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void print() {
        System.out.println("Book Title: " + title + "\n");
        System.out.println("Authors:");
        for (Author author : authors) {
            author.print();
        }
        System.out.println(" ");
        for (Element e : element) {
            e.print();
        }
    }

    public void addContent(Element e) {
        element.add(e);
    }

}
