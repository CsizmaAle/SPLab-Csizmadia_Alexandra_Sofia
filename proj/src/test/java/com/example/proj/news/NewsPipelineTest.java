package com.example.proj.news;

import com.example.proj.mitre.MitreImportService;
import com.example.proj.model.*;
import com.example.proj.persistence.NodeContentRepository;
import com.example.proj.persistence.NodeRepository;
import com.example.proj.persistence.RelationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest @Transactional
class NewsPipelineTest {
    @Autowired NewsPipeline pipeline;
    @Autowired SaveHandler save;
    @Autowired NodeRepository nodes;
    @Autowired RelationRepository relations;
    @Autowired NodeContentRepository contents;
    @Autowired MitreImportService importer;

    @BeforeEach void load() {
        importer.importFrom(getClass().getResourceAsStream("/stix-mini.json"));
        nodes.save(new Node(NodeType.SOFTWARE, "Ping", Source.MITRE));
    }
    Article art(String title, String url) { return new Article(title, "", url, Instant.now(), "body"); }
    Node ext(String id) { return nodes.findByExternalId(id).orElseThrow(); }

    @Test void savesNewsWithContent() {
        Node n = pipeline.process(art("Some news", "https://x/0")).orElseThrow();
        assertThat(n.getType()).isEqualTo(NodeType.NEWS);
        assertThat(n.getSource()).isEqualTo(Source.FEED);
        assertThat(contents.findByNodeId(n.getId())).get().extracting(NodeContent::getBody).isEqualTo("body");
    }

    @Test void saveHandlerCopiesFieldsAndSuggestsMatches() {
        Instant published = Instant.parse("2026-11-25T10:30:00Z");
        NewsContext c = new NewsContext(new Article("Titlu", "rezumat", "https://x/s", published, "corp"));
        c.tag = "malware";
        c.matches.addAll(List.of(ext("G0032"), ext("S0366")));
        save.handle(c);
        assertThat(c.saved).extracting(Node::getName, Node::getText, Node::getUrl, Node::getTag, Node::getCreatedAt)
            .containsExactly("Titlu", "rezumat", "https://x/s", "malware", published);
        assertThat(relations.findByChildIdAndKind(c.saved.getId(), RelationKind.SUGGESTED))
            .extracting(r -> r.getParent().getExternalId()).containsExactlyInAnyOrder("G0032", "S0366");
    }

    @Test void handlerPassesToNext() {
        List<Node> seen = new ArrayList<>();
        NewsHandler spy = new NewsHandler() { @Override public void handle(NewsContext c) { seen.add(c.saved); } };
        NewsHandler first = new NewsHandler() { @Override public void handle(NewsContext c) { pass(c); } };
        assertThat(first.setNext(spy)).isSameAs(spy);
        NewsContext c = new NewsContext(art("t", "https://x/n"));
        first.handle(c);
        assertThat(seen).hasSize(1);
        assertThat(c.tag).isEqualTo("other");
    }

    @Test void longTitleIsCutAndLongUrlIsSkipped() {
        assertThat(pipeline.process(art("t".repeat(300), "https://x/long-title")).orElseThrow().getName()).hasSize(255);
        assertThat(pipeline.process(art("url lung", "https://x/" + "u".repeat(300)))).isEmpty();
    }

    @Test void skipsDuplicates() {
        assertThat(pipeline.process(art("a", "https://x/1"))).isPresent();
        long count = nodes.count();
        assertThat(pipeline.process(art("a again", "https://x/1"))).isEmpty();
        assertThat(nodes.count()).isEqualTo(count);
    }

    @ParameterizedTest @CsvSource({
        "New RANSOMWARE strain, '', ransomware",
        "CVE-2026-1234 patched, '', vulnerability",
        "Weather today, '', other",
        "Phishing kit sold, '', phishing",
        "Chrome Vulnerability fixed, '', vulnerability",
        "Zero-Day in routers, '', vulnerability",
        "Retailer data breach, '', breach",
        "Passwords leaked, '', breach",
        "New malware family, '', malware",
        "Banking trojan returns, '', malware",
        "Backdoor in plugin, '', malware",
        "Quiet title, 'a phishing campaign', phishing",
        "Ransomware gang exploits CVE-2026-1, '', ransomware",
        "Data leak via backdoor, '', breach"})
    void tagsByKeywords(String title, String summary, String tag) {
        Article a = new Article(title, summary, "https://x/tag/" + title.hashCode(), Instant.now(), "");
        assertThat(pipeline.process(a).orElseThrow().getTag()).isEqualTo(tag);
    }

    List<String> suggested(String title, String summary) {
        Node n = pipeline.process(new Article(title, summary, "https://x/m/" + title.hashCode(), Instant.now(), "")).orElseThrow();
        return relations.findByChildIdAndKind(n.getId(), RelationKind.SUGGESTED).stream().map(r -> r.getParent().getName()).toList();
    }

    @Test void suggestsMatchingNodes() {
        Node n = pipeline.process(art("Lazarus Group ransomware attack", "https://x/3")).orElseThrow();
        assertThat(relations.findByChildIdAndKind(n.getId(), RelationKind.SUGGESTED))
            .extracting(r -> r.getParent().getName()).containsExactly("Lazarus Group");
    }

    @Test void matchesWholeWordsOnly() {
        Node n = pipeline.process(art("Pinging servers with new tricks", "https://x/4")).orElseThrow();
        assertThat(relations.findByChildIdAndKind(n.getId(), RelationKind.SUGGESTED)).isEmpty();
    }

    @Test void matchRules() {
        nodes.save(new Node(NodeType.GROUP, "LAPSUS$", Source.MITRE));
        nodes.save(new Node(NodeType.SOFTWARE, "Net", Source.MITRE));
        assertThat(suggested("Ping sweep, then WannaCry", "")).containsExactlyInAnyOrder("Ping", "WannaCry");
        assertThat(suggested("Quiet title", "Lazarus Group again")).containsExactly("Lazarus Group");
        assertThat(suggested("lazarus group and wannacry", "")).isEmpty();
        assertThat(suggested("LAPSUS$ leaks code", "")).containsExactly("LAPSUS$");
        assertThat(suggested("Net income grows", "")).isEmpty();
        assertThat(suggested("ReWannaCry clone spotted", "")).isEmpty(); // lipit in stanga
        assertThat(suggested("Data Encrypted for Impact explained", "")).isEmpty(); // tehnicile nu se sugereaza
    }
}
