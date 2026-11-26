package com.example.proj.controllers;

import com.example.proj.model.NewNodeRequest;
import com.example.proj.model.Node;
import com.example.proj.model.NodeType;
import com.example.proj.model.Relation;
import com.example.proj.model.RelationKind;
import com.example.proj.model.Source;
import com.example.proj.persistence.NodeRepository;
import com.example.proj.persistence.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

import static com.example.proj.controllers.Found.found;

@RestController
public class NodeController {
    @Autowired private NodeRepository nodes;
    @Autowired private RelationRepository relations;

    @PostMapping("/nodes") @ResponseStatus(HttpStatus.CREATED)
    public Node addRoot(@RequestBody NewNodeRequest req) {
        if (req.type() != NodeType.GROUP && req.type() != NodeType.TACTIC)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "doar GROUP sau TACTIC pot fi radacini");
        return nodes.save(req.toNode());
    }

    @PostMapping("/nodes/{parentId}/children") @ResponseStatus(HttpStatus.CREATED) @Transactional
    public Node addChild(@PathVariable Long parentId, @RequestBody NewNodeRequest req) {
        Node parent = found(nodes.findById(parentId)), child = nodes.save(req.toNode());
        relations.save(new Relation(parent, child, RelationKind.ATTACHED));
        return child;
    }

    @DeleteMapping("/nodes/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @Transactional
    public void delete(@PathVariable Long id) {
        Node n = found(nodes.findById(id));
        if (n.getSource() != Source.CUSTOM) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "doar nodurile proprii se pot sterge");
        if (!relations.findChildren(id, List.of(RelationKind.ATTACHED)).isEmpty())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "sterge intai nodurile atasate");
        relations.deleteByParentIdOrChildId(id, id);
        nodes.delete(n);
    }
}
