package com.example.proj.persistence;

import com.example.proj.model.Node;
import com.example.proj.model.NodeType;
import com.example.proj.model.Source;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface NodeRepository extends JpaRepository<Node, Long> {
    Optional<Node> findByExternalId(String externalId);
    List<Node> findByTypeOrderByName(NodeType type);
    List<Node> findBySource(Source source);
    boolean existsByUrl(String url);

    // locate, nu like: % si _ din text se cauta ca atare
    @Query("""
        select n from Node n where (locate(lower(:q), lower(n.name)) > 0 or locate(lower(:q), lower(n.text)) > 0)
        and (:type is null or n.type = :type) order by n.name""")
    List<Node> search(String q, NodeType type, Pageable page);
}
