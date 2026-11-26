package com.example.proj.persistence;

import com.example.proj.model.Node;
import com.example.proj.model.NodeType;
import com.example.proj.model.Source;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
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

    Node save(NodeType type, String name, String text) {
        Node n = new Node(type, name, Source.CUSTOM);
        n.setText(text);
        return nodes.save(n);
    }

    @Test void searchesNameAndTextCaseInsensitive() {
        save(NodeType.NOTE, "x", "lazarus again"); // salvata prima, ca sa conteze ordonarea dupa nume
        save(NodeType.GROUP, "Lazarus Group", null);
        save(NodeType.SOFTWARE, "WannaCry", null);
        assertThat(nodes.search("LAZARUS", null, PageRequest.of(0, 50))).extracting(Node::getName).containsExactly("Lazarus Group", "x");
        assertThat(nodes.search("LAZARUS", NodeType.NOTE, PageRequest.of(0, 50))).extracting(Node::getName).containsExactly("x");
        assertThat(nodes.search("lazarus", null, PageRequest.of(0, 1))).extracting(Node::getName).containsExactly("Lazarus Group");
    }

    @Test void wildcardsAreSearchedLiterally() {
        save(NodeType.NOTE, "100% sigur", null);
        save(NodeType.NOTE, "altceva", "fara procent");
        assertThat(nodes.search("%", null, PageRequest.of(0, 50))).extracting(Node::getName).containsExactly("100% sigur");
        assertThat(nodes.search("_", null, PageRequest.of(0, 50))).isEmpty();
    }
}
