package com.example.proj.news;

import com.example.proj.model.Node;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Component
public class NewsPipeline {
    private final NewsHandler first;

    NewsPipeline(SaveHandler save) { first = save; }

    @Transactional
    public Optional<Node> process(Article a) {
        NewsContext c = new NewsContext(a);
        first.handle(c);
        return Optional.ofNullable(c.saved);
    }
}
