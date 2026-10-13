package classses;

import java.util.ArrayList;
import java.util.List;

public class TableOfContents implements Element {
    private String title;
    private List<String> chapters = new ArrayList<>();
    private List<Integer> pages = new ArrayList<>();

    public TableOfContents(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void addEntry(String chapter, int page) {
        chapters.add(chapter);
        pages.add(page);
    }

    public List<String> getChapters() {
        return chapters;
    }

    public List<Integer> getPages() {
        return pages;
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


    @Override
    public void accept(Visitor v){
        v.visit(this);
    }


}
