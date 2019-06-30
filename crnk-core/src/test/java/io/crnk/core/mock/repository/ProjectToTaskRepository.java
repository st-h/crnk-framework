package io.crnk.core.mock.repository;

import io.crnk.core.mock.models.Project;
import io.crnk.core.mock.models.Task;
import io.crnk.core.mock.repository.util.Relation;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.OneRelationshipRepository;
import io.crnk.core.repository.RelationshipMatcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProjectToTaskRepository extends AbstractRelationShipRepository<Project> implements OneRelationshipRepository<Project, Long, Task, Long> {

    private final static ConcurrentMap<Relation<Project>, Integer> STATIC_REPOSITORY = new ConcurrentHashMap<>();

    public static void clear() {
        STATIC_REPOSITORY.clear();
    }

    @Override
    public ConcurrentMap<Relation<Project>, Integer> getRepo() {
        return STATIC_REPOSITORY;
    }

    @Override
    public RelationshipMatcher getMatcher() {
        return null;
    }

    @Override
    public Map<Long, Task> findOneRelations(Collection<Long> sourceIds, String fieldName, QuerySpec querySpec) {
        Map<Long, Task> map = new HashMap<>();
        for (Long sourceId : sourceIds) {
            Map<Relation<Project>, Integer> repo = getRepo();
            for (Relation<Project> relation : repo.keySet()) {
                if (relation.getSource().getId().equals(sourceId) &&
                        relation.getFieldName().equals(fieldName)) {
                    Task task = new Task();
                    task.setId((Long) relation.getTargetId());
                    map.put(sourceId, task);
                    break;
                }
            }
            return null;
        }
        return map;
    }
}
