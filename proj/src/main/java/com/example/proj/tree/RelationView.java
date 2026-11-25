package com.example.proj.tree;

import com.example.proj.model.Node;
import com.example.proj.model.NodeType;
import com.example.proj.model.RelationKind;
import com.example.proj.persistence.NodeRepository;
import com.example.proj.persistence.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

abstract class RelationView implements TreeView {
    @Autowired private NodeRepository nodes;
    @Autowired private RelationRepository relations;
    private final NodeType rootType;
    private final List<RelationKind> kinds;

    RelationView(NodeType rootType, RelationKind... kinds) {
        this.rootType = rootType;
        this.kinds = List.of(kinds);
    }

    @Override public List<Node> roots() { return nodes.findByTypeOrderByName(rootType); }
    @Override public List<Node> children(Node parent) { return relations.findChildren(parent.getId(), kinds); }
}
