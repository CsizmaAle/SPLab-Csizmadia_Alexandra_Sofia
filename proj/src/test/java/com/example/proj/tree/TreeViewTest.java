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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest @Transactional
class TreeViewTest {
    @Autowired MitreImportService importer;
    @Autowired @Qualifier("groups") TreeView groups;
    @Autowired @Qualifier("tactics") TreeView tactics;
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

    @Test void tacticsView() {
        assertThat(tactics.roots()).extracting(Node::getName).containsExactly("Execution", "Impact");
        assertThat(tactics.children(ext("TA0002"))).extracting(Node::getExternalId).containsExactly("T1059");
        assertThat(tactics.children(ext("T1059"))).extracting(Node::getExternalId).containsExactly("T1059.001");
        assertThat(tactics.children(ext("S0366"))).isEmpty(); // USES tine de vederea pe grupari
    }

    @Test void attachedNodesShowInBothViews() {
        link(ext("T1486"), "nota", RelationKind.ATTACHED);
        assertThat(groups.children(ext("T1486"))).extracting(Node::getName).containsExactly("nota");
        assertThat(tactics.children(ext("T1486"))).extracting(Node::getName).containsExactly("nota");
    }

    @Test void subtreeFollowsView() {
        TreeNode t = groups.subtree(ext("G0032"));
        assertThat(t.children()).extracting(c -> c.node().getName()).containsExactly("WannaCry");
        assertThat(t.children().getFirst().children()).extracting(c -> c.node().getExternalId()).containsExactly("T1486");
    }

    @Test void subtreeStopsOnCycle() {
        relations.save(new Relation(ext("T1486"), ext("G0032"), RelationKind.ATTACHED));
        TreeNode technique = groups.subtree(ext("G0032")).children().getFirst().children().getFirst();
        assertThat(technique.children()).containsExactly(new TreeNode(ext("G0032"), List.of()));
    }

    @Test void sharedNodeIsExpandedOnlyOnce() {
        relations.save(new Relation(ext("G0032"), ext("S0002"), RelationKind.USES));
        relations.save(new Relation(ext("S0002"), ext("T1486"), RelationKind.USES));
        link(ext("T1486"), "nota", RelationKind.ATTACHED);
        List<TreeNode> software = groups.subtree(ext("G0032")).children(); // Mimikatz, WannaCry
        assertThat(software.get(0).children().getFirst().children()).hasSize(1);
        assertThat(software.get(1).children()).containsExactly(new TreeNode(ext("T1486"), List.of()));
    }
}
