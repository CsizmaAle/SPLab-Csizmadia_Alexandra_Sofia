package com.example.proj.summary;

import com.example.proj.model.Node;
import com.example.proj.model.NodeType;
import com.example.proj.tree.TreeNode;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// numara nodurile unice pe tip (radacina nu se numara) si aduna valoarea impactului
public class SummaryVisitor implements TreeVisitor {
    private final Set<Long> seen = new HashSet<>();
    private final Map<NodeType, Long> counts = new EnumMap<>(NodeType.class);
    private Node root;
    private double impactTotal;

    @Override
    public void visit(TreeNode t) {
        Node n = t.node();
        if (!seen.add(n.getId())) return;
        if (root == null) { root = n; return; }
        counts.merge(n.getType(), 1L, Long::sum);
        if (n.getType() == NodeType.IMPACT && n.getValue() != null) impactTotal += n.getValue();
    }

    public Summary result() { return new Summary(root.getId(), root.getName(), counts, impactTotal); }
}
