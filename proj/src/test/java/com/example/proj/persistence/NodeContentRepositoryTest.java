package com.example.proj.persistence;

import com.example.proj.model.Node;
import com.example.proj.model.NodeContent;
import com.example.proj.model.NodeType;
import com.example.proj.model.Source;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest @Transactional
class NodeContentRepositoryTest {
    @Autowired NodeRepository nodes;
    @Autowired NodeContentRepository contents;
    @Autowired EntityManager em;

    @Test void savesLongBody() {
        Node n = nodes.save(new Node(NodeType.NEWS, "stire", Source.FEED));
        contents.saveAndFlush(new NodeContent(n, "x".repeat(50_000)));
        em.clear(); // forteaza citirea din baza, nu din cache
        assertThat(contents.findByNodeId(n.getId()).orElseThrow().getBody()).hasSize(50_000);
    }
}
