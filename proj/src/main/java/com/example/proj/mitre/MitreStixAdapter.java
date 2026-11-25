package com.example.proj.mitre;

import com.example.proj.mitre.MitreData.Link;
import com.example.proj.model.Node;
import com.example.proj.model.NodeType;
import com.example.proj.model.RelationKind;
import com.example.proj.model.Source;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import java.io.InputStream;
import java.util.*;

@Component
public class MitreStixAdapter {
    private static final Map<String, NodeType> TYPES = Map.of(
        "intrusion-set", NodeType.GROUP, "malware", NodeType.SOFTWARE, "tool", NodeType.SOFTWARE,
        "attack-pattern", NodeType.TECHNIQUE, "x-mitre-tactic", NodeType.TACTIC);

    public MitreData adapt(InputStream stixJson) {
        List<JsonNode> objects = JsonMapper.shared().readTree(stixJson).path("objects").valueStream()
            .filter(o -> !flag(o, "revoked") && !flag(o, "x_mitre_deprecated")).toList();
        Map<String, Node> byStixId = new LinkedHashMap<>();
        Map<String, String> tactics = new HashMap<>(); // x_mitre_shortname -> externalId
        for (JsonNode o : objects) {
            NodeType type = TYPES.get(str(o, "type"));
            if (type == null) continue;
            for (JsonNode ref : o.path("external_references")) {
                if (!"mitre-attack".equals(str(ref, "source_name"))) continue;
                Node n = new Node(type, str(o, "name"), Source.MITRE);
                n.setText(str(o, "description"));
                n.setExternalId(str(ref, "external_id"));
                n.setUrl(str(ref, "url"));
                byStixId.put(str(o, "id"), n);
                if (type == NodeType.TACTIC) tactics.put(str(o, "x_mitre_shortname"), n.getExternalId());
            }
        }
        List<Link> links = new ArrayList<>();
        for (JsonNode o : objects) {
            Node n = byStixId.get(str(o, "id")), from = byStixId.get(str(o, "source_ref")), to = byStixId.get(str(o, "target_ref"));
            if (n != null && !flag(o, "x_mitre_is_subtechnique"))
                for (JsonNode phase : o.path("kill_chain_phases")) {
                    String tactic = tactics.get(str(phase, "phase_name"));
                    if (tactic != null) links.add(new Link(tactic, n.getExternalId(), RelationKind.IN_TACTIC));
                }
            if (from == null || to == null) continue;
            String kind = str(o, "relationship_type");
            if ("subtechnique-of".equals(kind))
                links.add(new Link(to.getExternalId(), from.getExternalId(), RelationKind.SUBTECHNIQUE));
            else if ("uses".equals(kind) && (from.getType() == NodeType.GROUP && to.getType() == NodeType.SOFTWARE
                    || from.getType() == NodeType.SOFTWARE && to.getType() == NodeType.TECHNIQUE))
                links.add(new Link(from.getExternalId(), to.getExternalId(), RelationKind.USES));
        }
        return new MitreData(List.copyOf(byStixId.values()), links);
    }

    private static String str(JsonNode n, String field) { return n.path(field).asString(null); }

    private static boolean flag(JsonNode n, String field) { return n.path(field).asBoolean(false); }
}
