package com.example.proj.mitre;

import com.example.proj.model.Node;
import com.example.proj.model.NodeType;
import com.example.proj.model.Source;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MitreStixAdapter {
    private static final Map<String, NodeType> TYPES = Map.of(
        "intrusion-set", NodeType.GROUP, "malware", NodeType.SOFTWARE, "tool", NodeType.SOFTWARE,
        "attack-pattern", NodeType.TECHNIQUE, "x-mitre-tactic", NodeType.TACTIC);

    public MitreData adapt(InputStream stixJson) {
        List<Node> nodes = new ArrayList<>();
        for (JsonNode o : JsonMapper.shared().readTree(stixJson).path("objects")) {
            NodeType type = TYPES.get(str(o, "type"));
            if (type == null || o.path("revoked").asBoolean(false) || o.path("x_mitre_deprecated").asBoolean(false)) continue;
            for (JsonNode ref : o.path("external_references")) {
                if (!"mitre-attack".equals(str(ref, "source_name"))) continue;
                Node n = new Node(type, str(o, "name"), Source.MITRE);
                n.setText(str(o, "description"));
                n.setExternalId(str(ref, "external_id"));
                n.setUrl(str(ref, "url"));
                nodes.add(n);
            }
        }
        return new MitreData(nodes, List.of());
    }

    private static String str(JsonNode n, String field) { return n.path(field).asString(null); }
}
