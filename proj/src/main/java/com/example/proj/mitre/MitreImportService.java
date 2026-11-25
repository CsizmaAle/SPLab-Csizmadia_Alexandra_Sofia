package com.example.proj.mitre;

import com.example.proj.model.Node;
import com.example.proj.model.Relation;
import com.example.proj.model.RelationKind;
import com.example.proj.model.Source;
import com.example.proj.persistence.NodeRepository;
import com.example.proj.persistence.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class MitreImportService {
    @Autowired private MitreStixAdapter adapter;
    @Autowired private NodeRepository nodes;
    @Autowired private RelationRepository relations;

    @Transactional
    public ImportResult importFrom(InputStream stixJson) {
        MitreData data = adapter.adapt(stixJson);
        Map<String, Node> byExtId = nodes.findBySource(Source.MITRE).stream().collect(toMap(Node::getExternalId, n -> n));
        for (Node n : data.nodes()) {
            Node old = byExtId.putIfAbsent(n.getExternalId(), n);
            if (old == null) continue;
            old.setName(n.getName());
            old.setText(n.getText());
            old.setUrl(n.getUrl());
        }
        nodes.saveAll(byExtId.values());
        relations.deleteByKindIn(List.of(RelationKind.USES, RelationKind.SUBTECHNIQUE, RelationKind.IN_TACTIC));
        relations.saveAll(data.links().stream()
            .map(l -> new Relation(byExtId.get(l.parentExtId()), byExtId.get(l.childExtId()), l.kind())).toList());
        return new ImportResult(data.nodes().size(), data.links().size());
    }
}
