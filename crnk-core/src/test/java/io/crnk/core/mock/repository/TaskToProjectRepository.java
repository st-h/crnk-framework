package io.crnk.core.mock.repository;

import io.crnk.core.mock.models.FancyProject;
import io.crnk.core.mock.models.Project;
import io.crnk.core.mock.models.Task;
import io.crnk.core.mock.repository.util.Relation;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ManyRelationshipRepository;
import io.crnk.core.repository.OneRelationshipRepository;
import io.crnk.core.repository.RelationshipMatcher;
import io.crnk.core.resource.list.DefaultResourceList;
import io.crnk.core.resource.list.ResourceList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TaskToProjectRepository extends AbstractRelationShipRepository<Task>
        implements OneRelationshipRepository<Task, Long, Project, Long>, ManyRelationshipRepository<Task, Long, Project, Long> {

    private final static ConcurrentMap<Relation<Task>, Integer> STATIC_REPOSITORY = new ConcurrentHashMap<>();


    public static void clear() {
        STATIC_REPOSITORY.clear();
    }

    @Override
    ConcurrentMap<Relation<Task>, Integer> getRepo() {
        return STATIC_REPOSITORY;
    }

    @Override
    public RelationshipMatcher getMatcher() {
        RelationshipMatcher matcher = new RelationshipMatcher();
        matcher.rule().source(Task.class).target(Project.class).add();
        return matcher;
    }

    @Override
    public void setRelation(Task source, Long targetId, String fieldName) {
        super.setRelation(source, targetId, fieldName);
    }

    @Override
    public Map<Long, Project> findOneRelations(Collection<Long> sourceIds, String fieldName, QuerySpec querySpec) {
        Map<Long, Project> map = new HashMap<>();
        for (Long sourceId : sourceIds) {
            Map<Relation<Task>, Integer> repo = getRepo();
            for (Relation<Task> relation : repo.keySet()) {
                if (relation.getSource().getId().equals(sourceId) &&
                        relation.getFieldName().equals(fieldName)) {
                    Project project = new Project();
                    if (relation.getTargetId().equals(ProjectRepository.FANCY_PROJECT_ID)) {
                        project = new FancyProject();
                    }
                    project.setId((Long) relation.getTargetId());
                    map.put(sourceId, project);
                    break;
                }
            }
        }
        return map;
    }

    @Override
    public void setRelations(Task source, Iterable<Long> targetIds, String fieldName) {
        super.setRelations(source, targetIds, fieldName);
    }

    @Override
    public void addRelations(Task source, Iterable<Long> targetIds, String fieldName) {
        super.addRelations(source, targetIds, fieldName);
    }

    @Override
    public void removeRelations(Task source, Iterable<Long> targetIds, String fieldName) {
        super.removeRelations(source, targetIds, fieldName);
    }


    @Override
    public void setRelations(Task source, Collection<Long> targetIds, String fieldName) {

    }

    @Override
    public void addRelations(Task source, Collection<Long> targetIds, String fieldName) {

    }

    @Override
    public void removeRelations(Task source, Collection<Long> targetIds, String fieldName) {

    }

    @Override
    public Map<Long, ResourceList<Project>> findManyRelations(Collection<Long> sourceIds, String fieldName, QuerySpec querySpec) {
        Map<Long, ResourceList<Project>> map = new HashMap<>();
        for (Long sourceId : sourceIds) {
            ResourceList<Project> projects = new DefaultResourceList<>();
            for (Relation<Task> relation : getRepo().keySet()) {
                if (relation.getSource().getId().equals(sourceId) && relation.getFieldName().equals(fieldName)) {
                    Project project = new Project();
                    if (relation.getTargetId().equals(ProjectRepository.FANCY_PROJECT_ID)) {
                        project = new FancyProject();
                    }
                    project.setId((Long) relation.getTargetId());
                    projects.add(project);
                }
            }
            map.put(sourceId, projects);
        }
        return map;
    }
}
