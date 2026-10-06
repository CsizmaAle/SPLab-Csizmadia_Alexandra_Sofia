package classses;

import java.awt.Dimension;

public class ImageProxy implements Element {
    private String url;
    private Dimension dim;
    private Image realImg;

    public ImageProxy(String url) {
        this.url = url;
    }

    private Image loadImage() {
        if (realImg == null) {
            realImg = new Image(url);
        }
        return realImg;
    }

    @Override
    public void print() {
        loadImage().print();
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
