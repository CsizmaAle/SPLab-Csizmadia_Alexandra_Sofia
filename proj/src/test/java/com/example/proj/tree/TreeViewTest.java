package com.example.proj.tree;

import com.example.proj.mitre.MitreImportService;
import com.example.proj.model.*;
import com.example.proj.persistence.NodeRepository;
import com.example.proj.persistence.RelationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest @Transactional
class TreeViewTest {
    @Autowired MitreImportService importer;
    @Autowired @Qualifier("groups") TreeView groups;
    @Autowired NodeRepository nodes;
    @Autowired RelationRepository relations;

    @BeforeEach void load() { importer.importFrom(getClass().getResourceAsStream("/stix-mini.json")); }
    Node ext(String id) { return nodes.findByExternalId(id).orElseThrow(); }

    void link(Node parent, String name, RelationKind kind) {
        relations.save(new Relation(parent, nodes.save(new Node(NodeType.NOTE, name, Source.CUSTOM)), kind));
    }

    @Test void groupsView() {
        assertThat(groups.roots()).extracting(Node::getName).containsExactly("Lazarus Group");
        assertThat(groups.children(ext("G0032"))).extracting(Node::getName).containsExactly("WannaCry");
        assertThat(groups.children(ext("S0366"))).extracting(Node::getExternalId).containsExactly("T1486");
        assertThat(groups.children(ext("T1059"))).isEmpty(); // SUBTECHNIQUE tine de vederea pe tactici
    }

    @Test void childrenAreSortedAndIncludeOnlyAttached() {
        link(ext("G0032"), "Zeta", RelationKind.ATTACHED);
        link(ext("G0032"), "Alfa", RelationKind.ATTACHED);
        link(ext("G0032"), "Sugestie", RelationKind.SUGGESTED);
        assertThat(groups.children(ext("G0032"))).extracting(Node::getName).containsExactly("Alfa", "WannaCry", "Zeta");
    }
}
