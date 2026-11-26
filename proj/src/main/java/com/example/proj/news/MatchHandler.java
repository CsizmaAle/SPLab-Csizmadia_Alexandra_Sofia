package com.example.proj.news;

import com.example.proj.model.NodeType;
import com.example.proj.persistence.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;
import java.util.stream.Stream;

// gruparile si software-ul mentionate in titlu + rezumat, ca cuvant intreg si cu aceleasi majuscule
@Component
public class MatchHandler extends NewsHandler {
    @Autowired private NodeRepository nodes;

    @Override
    public void handle(NewsContext c) {
        String text = c.article.title() + " " + c.article.summary();
        Stream.concat(nodes.findByTypeOrderByName(NodeType.GROUP).stream(), nodes.findByTypeOrderByName(NodeType.SOFTWARE).stream())
            .filter(n -> n.getName().length() >= 4)
            // nu \b: nume ca "LAPSUS$" se termina cu un caracter care nu e litera
            .filter(n -> Pattern.compile("(?<!\\w)" + Pattern.quote(n.getName()) + "(?!\\w)").matcher(text).find())
            .forEach(c.matches::add);
        pass(c);
    }
}
