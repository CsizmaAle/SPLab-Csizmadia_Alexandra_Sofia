package com.example.proj.tree;

import com.example.proj.model.NodeType;
import com.example.proj.model.RelationKind;
import org.springframework.stereotype.Component;

@Component("groups")
class GroupsView extends RelationView {
    GroupsView() { super(NodeType.GROUP, RelationKind.USES, RelationKind.ATTACHED); }
}
