package classses;

public class GenerateToC implements Visitor {
    private TableOfContents toc = new TableOfContents("Table of Contents");
    private int currentPage = 0;

    @Override
    public void visit(Book b) {
    }

    @Override
    public void visit(Section s) {
        toc.addEntry(s.getTitle(), currentPage + 1);
    }

    @Override
    public void visit(Image p) {
        currentPage++;
    }

    @Override
    public void visit(Paragraph p) {
        currentPage++;
    }

    @Override
    public void visit(Table t) {
        currentPage++;
    }

    @Override
    public void visit(ImageProxy p) {
        currentPage++;
    }

    @Override
    public void visit(TableOfContents t) {
        currentPage++;
    }

    public TableOfContents getToC() {
        return toc;
    }
}
