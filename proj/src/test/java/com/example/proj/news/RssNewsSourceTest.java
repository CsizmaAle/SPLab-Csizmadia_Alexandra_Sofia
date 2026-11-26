package com.example.proj.news;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RssNewsSourceTest {
    String res(String f) { return getClass().getResource(f).toString(); }

    @Test void parsesItems() {
        Instant start = Instant.now();
        List<Article> a = new RssNewsSource(res("/rss-mini.xml")).fetch();
        assertThat(a).hasSize(2);
        assertThat(a.getFirst().title()).isEqualTo("Lazarus Group hits crypto exchange");
        assertThat(a.getFirst().summary()).isEqualTo("The North Korean group used new malware.");
        assertThat(a.getFirst().url()).isEqualTo("https://example.com/1");
        assertThat(a.getFirst().publishedAt()).isEqualTo(Instant.parse("2026-11-25T10:30:00Z"));
        assertThat(a.getFirst().body()).startsWith("<p>Full article");
        assertThat(a.get(1).publishedAt()).isBetween(start, Instant.now());
        assertThat(a.get(1).body()).isEqualTo("Attackers abuse PowerShell to ping internal servers.");
    }

    @Test void handlesOddItems() {
        Instant start = Instant.now();
        List<Article> a = new RssNewsSource(res("/rss-odd.xml")).fetch();
        assertThat(a).extracting(Article::title).containsExactly("Odd date", "Long");
        assertThat(a.getFirst().publishedAt()).isBetween(start, Instant.now());
        assertThat(a.getFirst().summary()).isEqualTo("Bold line & more ’quoted’");
        assertThat(a.get(1).publishedAt()).isEqualTo(Instant.parse("2025-11-05T06:00:00Z"));
        assertThat(a.get(1).summary()).hasSize(1000);
    }
}
