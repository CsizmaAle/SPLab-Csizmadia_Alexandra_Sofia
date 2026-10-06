package classses;

import java.util.ArrayList;
import java.util.List;

public class Book extends Section {
    private List<Author> authors = new ArrayList<>();

    public Book(String title, List<Author> authors, List<Element> element) {
        super(title);
        this.authors.addAll(authors);
        for (Element e : element) {
            add(e);
        }
    }

    public Book(String title) {
        super(title);
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public List<Author> getAuthors() {
        return authors;
    }

    @Override
    public void print() {
        System.out.println("Authors:");
        for (Author author : authors) {
            author.print();
        }
        System.out.println("");
        super.print();
    }

    public void addContent(Element e) {
        add(e);
    }
}
