package com.example.proj.news;

import com.example.proj.model.Node;
import java.util.ArrayList;
import java.util.List;

// ce afla verigile lantului despre o stire
public class NewsContext {
    public final Article article;
    public String tag = "other";
    public final List<Node> matches = new ArrayList<>();
    public Node saved; // null daca lantul s-a oprit

    public NewsContext(Article article) { this.article = article; }
}
