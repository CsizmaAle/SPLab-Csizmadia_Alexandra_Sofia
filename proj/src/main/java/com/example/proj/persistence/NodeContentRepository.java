package com.example.proj.persistence;

import com.example.proj.model.NodeContent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NodeContentRepository extends JpaRepository<NodeContent, Long> {
    Optional<NodeContent> findByNodeId(Long nodeId);
}
