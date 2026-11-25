package com.example.proj.mitre;

import com.example.proj.model.*;
import com.example.proj.persistence.NodeRepository;
import com.example.proj.persistence.RelationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest @Transactional
class MitreImportServiceTest {
    @Autowired MitreImportService service;
    @Autowired NodeRepository nodes;
    @Autowired RelationRepository relations;

    InputStream stix() { return getClass().getResourceAsStream("/stix-mini.json"); }

    @Test void importsNodesAndRelations() {
        assertThat(service.importFrom(stix())).isEqualTo(new ImportResult(8, 5));
        assertThat(nodes.findByTypeOrderByName(NodeType.GROUP)).extracting(Node::getName).containsExactly("Lazarus Group");
    }

    @Test void reimportKeepsUserDataAndDoesNotDuplicate() {
        service.importFrom(stix());
        Node lazarus = nodes.findByExternalId("G0032").orElseThrow();
        lazarus.setName("old name");
        lazarus.setText("old text");
        lazarus.setUrl("old url");
        Node note = nodes.save(new Node(NodeType.NOTE, "de citit", Source.CUSTOM));
        relations.save(new Relation(lazarus, note, RelationKind.ATTACHED));
        relations.save(new Relation(lazarus, note, RelationKind.SUGGESTED));

        service.importFrom(stix());

        assertThat(nodes.count()).isEqualTo(9);
        assertThat(nodes.findByExternalId("G0032").orElseThrow()).extracting(Node::getName, Node::getText, Node::getUrl)
            .containsExactly("Lazarus Group", "North Korean state-sponsored group.", "https://attack.mitre.org/groups/G0032");
        assertThat(relations.findByParentIdAndKindIn(lazarus.getId(), List.of(RelationKind.ATTACHED, RelationKind.SUGGESTED))).hasSize(2);
        assertThat(relations.count()).isEqualTo(7);
    }
}
