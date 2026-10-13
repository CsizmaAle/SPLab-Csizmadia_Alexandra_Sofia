package classses;

public class RenderContentVisitor implements Visitor{

    @Override
    public void visit(Book b){
        System.out.println("Book: " + b.getTitle());
        System.out.println("Authors:");
        for (Author author : b.getAuthors()) {
            author.print();
        }
        System.out.println();
    }

    @Override
    public void visit(Section s){
        System.out.println(s.getTitle());
    }

    @Override
    public void visit(Image p){
        System.out.println("Image with name: " + p.url());
    }

    @Override
    public void visit(Paragraph p){
        if (p.getTextAlignment() != null) {
            p.getTextAlignment().render(p, new Context(30));
        } else {
            System.out.println("Paragraph: " + p.getText());
        }
    }

    @Override
    public void visit(Table t){
        System.out.println("Table name: " + t.getTitle());
    }

    @Override
    public void visit(ImageProxy p){
        System.out.println("Image with name: " + p.url());
    }

    @Override
    public void visit(TableOfContents t){
        System.out.println(t.getTitle());
        for (int i = 0; i < t.getChapters().size(); i++) {
            System.out.println(t.getChapters().get(i) + " ........ " + t.getPages().get(i));
        }
    }
}
