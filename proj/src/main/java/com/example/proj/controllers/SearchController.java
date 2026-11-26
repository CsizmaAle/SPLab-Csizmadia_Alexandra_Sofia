package com.example.proj.controllers;

import com.example.proj.model.Node;
import com.example.proj.model.NodeType;
import com.example.proj.persistence.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
public class SearchController {
    @Autowired private NodeRepository nodes;

    @GetMapping("/search")
    public List<Node> search(@RequestParam String q, @RequestParam(required = false) NodeType type) {
        if (q.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "scrie ce cauti");
        return nodes.search(q.strip(), type, PageRequest.of(0, 50));
    }
}
