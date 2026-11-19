package com.example.proj.persistence;

import com.example.proj.model.Node;
import com.example.proj.model.NodeType;
import com.example.proj.model.Source;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest @Transactional
class NodeRepositoryTest {
    @Autowired NodeRepository nodes;

    @Test void savesAndFindsByExternalId() {
        Node g = new Node(NodeType.GROUP, "Lazarus Group", Source.MITRE);
        g.setExternalId("G0032");
        nodes.save(g);
        assertThat(nodes.findByExternalId("G0032")).get().extracting(Node::getName).isEqualTo("Lazarus Group");
    }

    @Test void findsByTypeSortedByName() {
        nodes.save(new Node(NodeType.GROUP, "b", Source.CUSTOM));
        nodes.save(new Node(NodeType.GROUP, "a", Source.CUSTOM));
        nodes.save(new Node(NodeType.NOTE, "c", Source.CUSTOM));
        assertThat(nodes.findByTypeOrderByName(NodeType.GROUP)).extracting(Node::getName).containsExactly("a", "b");
    }
}
