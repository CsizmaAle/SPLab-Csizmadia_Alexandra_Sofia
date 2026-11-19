package com.example.proj.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Node {
    @Id @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private NodeType type;
    private String name;
    @Column(length = 20000)
    private String text;
    private String url;
    @Column(name = "val") 
    private Double value;
    private String tag;
    @Column(unique = true)
    private String externalId;
    @Enumerated(EnumType.STRING)
    private Source source;
    private Instant createdAt = Instant.now();

    protected Node() {}

    public Node(NodeType type, String name, Source source) {
        this.type = type;
        this.name = name;
        this.source = source;
    }

    public Long getId() { return id; }
    public NodeType getType() { return type; }
    public void setType(NodeType type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    public Source getSource() { return source; }
    public void setSource(Source source) { this.source = source; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
