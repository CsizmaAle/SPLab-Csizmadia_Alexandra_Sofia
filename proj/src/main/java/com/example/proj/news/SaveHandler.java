package com.example.proj.news;

import com.example.proj.model.*;
import com.example.proj.persistence.NodeContentRepository;
import com.example.proj.persistence.NodeRepository;
import com.example.proj.persistence.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveHandler extends NewsHandler {
    @Autowired private NodeRepository nodes;
    @Autowired private NodeContentRepository contents;
    @Autowired private RelationRepository relations;

    @Override
    public void handle(NewsContext c) {
        Article a = c.article;
        if (a.url().length() > 255) return; // nu incape in coloana url
        Node n = new Node(NodeType.NEWS, a.title().substring(0, Math.min(255, a.title().length())), Source.FEED);
        n.setText(a.summary());
        n.setUrl(a.url());
        n.setTag(c.tag);
        n.setCreatedAt(a.publishedAt());
        c.saved = nodes.save(n);
        contents.save(new NodeContent(c.saved, a.body()));
        c.matches.forEach(m -> relations.save(new Relation(m, c.saved, RelationKind.SUGGESTED)));
        pass(c);
    }
}
