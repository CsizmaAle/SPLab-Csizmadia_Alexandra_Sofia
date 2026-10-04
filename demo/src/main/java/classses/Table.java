package classses;

public class Table implements Element {
    private String something;

    public Table(String something) {
        this.something = something;
    }

    public String getSomething() {
        return something;
    }

    @Override
    public void print() {
        System.out.println("Table name: " + something);
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
}
