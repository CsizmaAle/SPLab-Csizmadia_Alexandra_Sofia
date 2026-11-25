package com.example.proj.tree;

import com.example.proj.model.Node;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface TreeView {
    List<Node> roots();
    List<Node> children(Node parent);

    default TreeNode subtree(Node root) { return subtree(root, new HashSet<>()); }

    // un nod deja vizitat apare ca frunza
    private TreeNode subtree(Node n, Set<Long> visited) {
        return new TreeNode(n, visited.add(n.getId()) ? children(n).stream().map(c -> subtree(c, visited)).toList() : List.of());
    }
}
