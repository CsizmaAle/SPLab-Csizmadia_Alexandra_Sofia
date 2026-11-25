package com.example.proj.mitre;

import com.example.proj.model.Node;
import com.example.proj.model.RelationKind;
import java.util.List;

public record MitreData(List<Node> nodes, List<Link> links) {
    public record Link(String parentExtId, String childExtId, RelationKind kind) {}
}
