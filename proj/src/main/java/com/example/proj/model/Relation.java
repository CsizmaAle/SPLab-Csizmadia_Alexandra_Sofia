package com.example.proj.model;

import jakarta.persistence.*;

@Entity
public class Relation {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    private Node parent;
    @ManyToOne
    private Node child;
    @Enumerated(EnumType.STRING)
    private RelationKind kind;

    protected Relation() {}

    public Relation(Node parent, Node child, RelationKind kind) {
        this.parent = parent;
        this.child = child;
        this.kind = kind;
    }

    public Long getId() { return id; }
    public Node getParent() { return parent; }
    public Node getChild() { return child; }
    public RelationKind getKind() { return kind; }
}
