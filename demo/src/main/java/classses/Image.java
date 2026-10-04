package classses;

public class Image implements Element {
    private String name;

    public Image(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void print() {
        System.out.println("Image with name: " + name);
    }

    @Override
    public void add(Element element) {
        
    }

    @Override
    public void remove(Element element) {
        
    }

    @Override
    public Element get(int index) {
        
        return null;
    }



}
