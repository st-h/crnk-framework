package io.crnk.core.mock.repository;

import io.crnk.core.mock.models.Task;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.InMemoryResourceRepository;
import io.crnk.core.repository.MetaRepository;
import io.crnk.core.resource.meta.MetaInformation;

import java.util.Collection;

public class TaskRepository extends InMemoryResourceRepository<Task, Long> implements MetaRepository<Task> {

    public TaskRepository() {
        super(Task.class);
    }

    @Override
    public MetaInformation getMetaInformation(Collection<Task> resources, QuerySpec querySpec) {
        return new MetaData();
    }

    public static class MetaData implements MetaInformation {

        public String someValue;
    }
}
