package io.crnk.core.engine.internal.repository;

import io.crnk.core.engine.information.repository.RelationshipRepositoryInformation;
import io.crnk.core.engine.information.repository.ResourceRepositoryInformation;
import io.crnk.core.engine.information.resource.ResourceField;
import io.crnk.core.repository.ResourceRepository;


public interface RepositoryAdapterFactory {

    boolean accepts(Object repository);

    ResourceRepositoryAdapter createResourceRepositoryAdapter(ResourceRepositoryInformation information, ResourceRepository repository);

    RelationshipRepositoryAdapter createRelationshipRepositoryAdapter(ResourceField field, RelationshipRepositoryInformation information, Object repository);

    ResourceRepositoryAdapter decorate(ResourceRepositoryAdapter adapter);

    RelationshipRepositoryAdapter decorate(RelationshipRepositoryAdapter adapter);

}
