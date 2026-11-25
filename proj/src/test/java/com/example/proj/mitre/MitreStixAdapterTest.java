package com.example.proj.mitre;

import com.example.proj.model.Node;
import com.example.proj.model.NodeType;
import com.example.proj.model.Source;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;

class MitreStixAdapterTest {
    MitreData data = new MitreStixAdapter().adapt(getClass().getResourceAsStream("/stix-mini.json"));

    Map<String, Node> byId() { return data.nodes().stream().collect(toMap(Node::getExternalId, n -> n)); }

    @Test void mapsTypesAndSkipsRevoked() {
        assertThat(byId()).containsOnlyKeys("G0032", "S0366", "S0002", "T1486", "T1059", "T1059.001", "TA0040", "TA0002");
        assertThat(byId().get("S0002").getType()).isEqualTo(NodeType.SOFTWARE);
        assertThat(byId().get("TA0040").getType()).isEqualTo(NodeType.TACTIC);
        assertThat(byId().get("G0032").getSource()).isEqualTo(Source.MITRE);
        assertThat(byId().get("T1486").getText()).contains("encrypt");
        assertThat(byId().get("T1486").getUrl()).isEqualTo("https://attack.mitre.org/techniques/T1486");
    }
}
