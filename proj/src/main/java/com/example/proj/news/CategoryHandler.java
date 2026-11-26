package com.example.proj.news;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// primul grup de cuvinte cheie gasit in titlu + rezumat da eticheta
@Component
public class CategoryHandler extends NewsHandler {
    private static final List<Map.Entry<String, List<String>>> RULES = List.of(
        Map.entry("ransomware", List.of("ransomware")),
        Map.entry("phishing", List.of("phishing")),
        Map.entry("vulnerability", List.of("cve-", "vulnerab", "zero-day")),
        Map.entry("breach", List.of("breach", "leak")),
        Map.entry("malware", List.of("malware", "trojan", "backdoor")));

    @Override
    public void handle(NewsContext c) {
        String text = (c.article.title() + " " + c.article.summary()).toLowerCase(Locale.ROOT);
        c.tag = RULES.stream().filter(r -> r.getValue().stream().anyMatch(text::contains))
            .map(Map.Entry::getKey).findFirst().orElse("other");
        pass(c);
    }
}
