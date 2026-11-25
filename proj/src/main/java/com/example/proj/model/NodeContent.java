package com.example.proj.model;

import jakarta.persistence.*;

@Entity
public class NodeContent {
    @Id @GeneratedValue
    private Long id;
    @OneToOne
    private Node node;
    @Lob
    private String body;

    protected NodeContent() {}

    public NodeContent(Node node, String body) {
        this.node = node;
        this.body = body;
    }

    public Long getId() { return id; }
    public Node getNode() { return node; }
    public String getBody() { return body; }
}
