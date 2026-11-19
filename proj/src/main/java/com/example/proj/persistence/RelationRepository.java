package com.example.proj.persistence;

import com.example.proj.model.Relation;
import com.example.proj.model.RelationKind;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface RelationRepository extends JpaRepository<Relation, Long> {
    List<Relation> findByParentIdAndKindIn(Long parentId, Collection<RelationKind> kinds);
    List<Relation> findByChildIdAndKind(Long childId, RelationKind kind);
    boolean existsByParentIdAndChildIdAndKind(Long parentId, Long childId, RelationKind kind);
    void deleteByKindIn(Collection<RelationKind> kinds);
}
