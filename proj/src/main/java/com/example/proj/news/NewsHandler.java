package com.example.proj.news;

// veriga din Chain of Responsibility
public abstract class NewsHandler {
    private NewsHandler next;

    public NewsHandler setNext(NewsHandler next) {
        this.next = next;
        return next;
    }

    public abstract void handle(NewsContext c);

    protected void pass(NewsContext c) { if (next != null) next.handle(c); }
}
