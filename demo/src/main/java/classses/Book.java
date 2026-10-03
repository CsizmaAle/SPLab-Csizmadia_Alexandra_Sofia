package classses;

public class Book {
    private String title;
    private Author authors[];
    private Element element[];

    public Book(String title, Author[] authors, Element[] element) {
        this.title = title;
        this.authors = authors;
        this.element = element;
    }

    public String getTitle() {
        return title;
    }

    public Author[] getAuthors() {
        return authors;
    }

    public Element[] getElement() {
        return element;
    }

    public void print() {
        System.out.println("Book Title: " + title);
        System.out.println("Authors:");
        for (Author author : authors) {
            System.out.println("- " + author.getName());
        }
        System.out.println("Elements:");
        for (Element e : element) {
            System.out.println("- " + e.print());
        }
    }

}
