package classses;

import java.util.concurrent.TimeUnit;

public class Image implements Element {
    private String url;
    private ImageContent content;

    public Image(String url) {
        this.url = url;
        this.content = new ImageContent();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public ImageContent content() {
        return content;
    }

    @Override
    public void print() {
        System.out.println("Image with name: " + url);
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
