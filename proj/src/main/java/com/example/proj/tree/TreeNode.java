package com.example.proj.tree;

import com.example.proj.model.Node;
import java.util.List;

public record TreeNode(Node node, List<TreeNode> children) {}
