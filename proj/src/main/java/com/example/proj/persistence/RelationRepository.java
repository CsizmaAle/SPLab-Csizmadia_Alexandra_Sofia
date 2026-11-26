package com.example.proj.persistence;

import com.example.proj.model.Node;
import com.example.proj.model.Relation;
import com.example.proj.model.RelationKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Collection;
import java.util.List;

public interface RelationRepository extends JpaRepository<Relation, Long> {
    List<Relation> findByParentIdAndKindIn(Long parentId, Collection<RelationKind> kinds);
    List<Relation> findByChildIdAndKind(Long childId, RelationKind kind);
    boolean existsByParentIdAndChildIdAndKind(Long parentId, Long childId, RelationKind kind);
    @Query("select r.child from Relation r where r.parent.id = :parentId and r.kind in :kinds order by r.child.name")
    List<Node> findChildren(Long parentId, Collection<RelationKind> kinds);
    void deleteByKindIn(Collection<RelationKind> kinds);
    void deleteByParentIdOrChildId(Long parentId, Long childId);
}
