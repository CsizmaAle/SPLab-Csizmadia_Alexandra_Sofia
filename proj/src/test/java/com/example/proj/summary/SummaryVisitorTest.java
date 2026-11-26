package com.example.proj.summary;

import com.example.proj.mitre.MitreImportService;
import com.example.proj.model.*;
import com.example.proj.persistence.NodeRepository;
import com.example.proj.persistence.RelationRepository;
import com.example.proj.tree.TreeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest @Transactional
class SummaryVisitorTest {
    @Autowired MitreImportService importer;
    @Autowired @Qualifier("groups") TreeView groups;
    @Autowired NodeRepository nodes;
    @Autowired RelationRepository relations;

    @BeforeEach void load() { importer.importFrom(getClass().getResourceAsStream("/stix-mini.json")); }
    Node ext(String id) { return nodes.findByExternalId(id).orElseThrow(); }

    void impact(Node parent, Double value) { attach(parent, NodeType.IMPACT, value); }

    void attach(Node parent, NodeType type, Double value) {
        Node n = new Node(type, "Pagube", Source.CUSTOM);
        n.setValue(value);
        relations.save(new Relation(parent, nodes.save(n), RelationKind.ATTACHED));
    }

    Summary summary(Node root) {
        SummaryVisitor v = new SummaryVisitor();
        groups.subtree(root).accept(v);
        return v.result();
    }

    @Test void countsUniqueNodesAndImpact() {
        Node mimikatz = ext("S0002");
        relations.save(new Relation(ext("G0032"), mimikatz, RelationKind.USES));
        relations.save(new Relation(mimikatz, ext("T1486"), RelationKind.USES)); // aceeasi tehnica, 2 software
        impact(ext("S0366"), 4e9);

        Summary s = summary(ext("G0032"));
        assertThat(s.counts()).containsEntry(NodeType.SOFTWARE, 2L).containsEntry(NodeType.TECHNIQUE, 1L)
            .containsEntry(NodeType.IMPACT, 1L).doesNotContainKey(NodeType.GROUP);
        assertThat(s.impactTotal()).isEqualTo(4e9);
        assertThat(s).extracting(Summary::nodeId, Summary::name).containsExactly(ext("G0032").getId(), "Lazarus Group");
    }

    @Test void rootSeenAgainInCycleIsNotCounted() {
        relations.save(new Relation(ext("T1486"), ext("G0032"), RelationKind.ATTACHED));
        assertThat(summary(ext("G0032")).counts()).isEqualTo(Map.of(NodeType.SOFTWARE, 1L, NodeType.TECHNIQUE, 1L));
    }

    @Test void impactTotalSumsOnlyImpactValues() {
        impact(ext("G0032"), 1.5);
        impact(ext("S0366"), null); // se numara, dar nu adauga nimic
        impact(ext("S0366"), 2.0);
        attach(ext("G0032"), NodeType.NOTE, 7.0); // valoare pe alt tip: nu intra in total
        Summary s = summary(ext("G0032"));
        assertThat(s.counts()).containsEntry(NodeType.IMPACT, 3L).containsEntry(NodeType.NOTE, 1L);
        assertThat(s.impactTotal()).isEqualTo(3.5);
    }
}
