package com.example.proj.controllers;

import com.example.proj.model.NewNodeRequest;
import com.example.proj.model.Node;
import com.example.proj.model.Relation;
import com.example.proj.model.RelationKind;
import com.example.proj.persistence.NodeRepository;
import com.example.proj.persistence.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.example.proj.controllers.Found.found;

@RestController
public class NodeController {
    @Autowired private NodeRepository nodes;
    @Autowired private RelationRepository relations;

    @PostMapping("/nodes/{parentId}/children") @ResponseStatus(HttpStatus.CREATED) @Transactional
    public Node addChild(@PathVariable Long parentId, @RequestBody NewNodeRequest req) {
        Node parent = found(nodes.findById(parentId)), child = nodes.save(req.toNode());
        relations.save(new Relation(parent, child, RelationKind.ATTACHED));
        return child;
    }
}
