package com.example.proj.controllers;

import com.example.proj.model.Node;
import com.example.proj.persistence.NodeRepository;
import com.example.proj.summary.Summary;
import com.example.proj.summary.SummaryVisitor;
import com.example.proj.tree.TreeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.proj.controllers.Found.found;

@RestController
public class TreeController {
    @Autowired private Map<String, TreeView> views;
    @Autowired private NodeRepository nodes;

    @GetMapping("/tree/{view}")
    public List<Node> roots(@PathVariable String view) { return view(view).roots(); }

    @GetMapping("/tree/{view}/{nodeId}/children")
    public List<Node> children(@PathVariable String view, @PathVariable Long nodeId) { return view(view).children(node(nodeId)); }

    @GetMapping("/nodes/{id}")
    public Node node(@PathVariable Long id) { return found(nodes.findById(id)); }

    @GetMapping("/nodes/{id}/summary")
    public Summary summary(@PathVariable Long id, @RequestParam(defaultValue = "groups") String view) {
        SummaryVisitor v = new SummaryVisitor();
        view(view).subtree(node(id)).accept(v);
        return v.result();
    }

    private TreeView view(String name) { return found(Optional.ofNullable(views.get(name))); }
}
