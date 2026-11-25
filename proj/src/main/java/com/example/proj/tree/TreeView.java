package com.example.proj.tree;

import com.example.proj.model.Node;
import java.util.List;

public interface TreeView {
    List<Node> roots();
    List<Node> children(Node parent);
}
