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

    public void addContent(Element e) {
        add(e);
    }

    @Override
    public void accept(Visitor v){
        v.visit(this);
        for (Element child : getChildren()) {
            child.accept(v);
        }
    }
}
