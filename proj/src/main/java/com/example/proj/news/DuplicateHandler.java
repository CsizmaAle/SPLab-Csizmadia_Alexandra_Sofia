package com.example.proj.news;

import com.example.proj.persistence.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// o stire cu url deja salvat opreste lantul
@Component
public class DuplicateHandler extends NewsHandler {
    @Autowired private NodeRepository nodes;

    @Override
    public void handle(NewsContext c) { if (!nodes.existsByUrl(c.article.url())) pass(c); }
}
