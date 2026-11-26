package com.example.proj.model;

// validat la citirea JSON-ului: o exceptie aici devine 400
public record NewNodeRequest(NodeType type, String name, String text, String url, Double value) {
    public NewNodeRequest {
        if (type == null || name == null || name.isBlank()) throw new IllegalArgumentException("type si name sunt obligatorii");
        name = name.strip();
        if (name.length() > 255 || url != null && url.length() > 255 || text != null && text.length() > 20_000)
            throw new IllegalArgumentException("text prea lung");
    }

    public Node toNode() {
        Node n = new Node(type, name, Source.CUSTOM);
        n.setText(text);
        n.setUrl(url);
        n.setValue(value);
        return n;
    }
}
