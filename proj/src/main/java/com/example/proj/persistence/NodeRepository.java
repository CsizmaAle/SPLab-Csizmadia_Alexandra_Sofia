package com.example.proj.persistence;

import com.example.proj.model.Node;
import com.example.proj.model.NodeType;
import com.example.proj.model.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NodeRepository extends JpaRepository<Node, Long> {
    Optional<Node> findByExternalId(String externalId);
    List<Node> findByTypeOrderByName(NodeType type);
    List<Node> findBySource(Source source);
    boolean existsByUrl(String url);
}
