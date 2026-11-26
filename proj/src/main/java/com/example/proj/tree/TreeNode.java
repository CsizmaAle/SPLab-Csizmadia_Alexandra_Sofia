package com.example.proj.tree;

import com.example.proj.model.Node;
import com.example.proj.summary.TreeVisitor;
import java.util.List;

public record TreeNode(Node node, List<TreeNode> children) {
    // viziteaza nodul, apoi copiii
    public void accept(TreeVisitor v) {
        v.visit(this);
        children.forEach(c -> c.accept(v));
    }
}
