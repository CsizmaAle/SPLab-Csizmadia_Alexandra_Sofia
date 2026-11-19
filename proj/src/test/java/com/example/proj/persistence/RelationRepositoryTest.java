package com.example.proj.persistence;

import com.example.proj.model.Node;
import com.example.proj.model.NodeType;
import com.example.proj.model.Relation;
import com.example.proj.model.RelationKind;
import com.example.proj.model.Source;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest @Transactional
class RelationRepositoryTest {
    @Autowired NodeRepository nodes;
    @Autowired RelationRepository relations;

    @Test void findsChildrenByKind() {
        Node g = nodes.save(new Node(NodeType.GROUP, "Lazarus Group", Source.MITRE));
        Node s = nodes.save(new Node(NodeType.SOFTWARE, "WannaCry", Source.MITRE));
        Node n = nodes.save(new Node(NodeType.NOTE, "kill switch?", Source.CUSTOM));
        relations.save(new Relation(g, s, RelationKind.USES));
        relations.save(new Relation(g, n, RelationKind.ATTACHED));
        assertThat(relations.findByParentIdAndKindIn(g.getId(), List.of(RelationKind.USES))).hasSize(1);
        assertThat(relations.findByParentIdAndKindIn(g.getId(), List.of(RelationKind.USES, RelationKind.ATTACHED))).hasSize(2);
        assertThat(relations.existsByParentIdAndChildIdAndKind(g.getId(), n.getId(), RelationKind.ATTACHED)).isTrue();
    }

    @Test void deletesOnlyGivenKinds() {
        Node g = nodes.save(new Node(NodeType.GROUP, "g", Source.MITRE));
        Node s = nodes.save(new Node(NodeType.SOFTWARE, "s", Source.MITRE));
        relations.save(new Relation(g, s, RelationKind.USES));
        relations.save(new Relation(g, s, RelationKind.ATTACHED));
        relations.deleteByKindIn(List.of(RelationKind.USES));
        assertThat(relations.findAll()).extracting(Relation::getKind).containsExactly(RelationKind.ATTACHED);
    }
}
