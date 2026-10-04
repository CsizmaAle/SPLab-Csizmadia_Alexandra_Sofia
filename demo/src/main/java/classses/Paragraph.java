package classses;

public class Paragraph implements Element {
    private String text;

    public Paragraph(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public void add(Element element) {
        // TO DO
    }

    @Override
    public void remove(Element element) {
        // TO DO
    }

    @Override
    public Element get(int index) {
        // TO DO
        return null;
    }

    @Override 
    public void print() {
        System.out.println("Paragraph: " + text);
    }


}
