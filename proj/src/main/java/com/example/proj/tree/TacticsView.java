package com.example.proj.tree;

import com.example.proj.model.NodeType;
import com.example.proj.model.RelationKind;
import org.springframework.stereotype.Component;

@Component("tactics")
class TacticsView extends RelationView {
    TacticsView() { super(NodeType.TACTIC, RelationKind.IN_TACTIC, RelationKind.SUBTECHNIQUE, RelationKind.ATTACHED); }
}
