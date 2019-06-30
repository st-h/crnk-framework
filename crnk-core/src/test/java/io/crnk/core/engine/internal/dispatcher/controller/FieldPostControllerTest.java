package io.crnk.core.engine.internal.dispatcher.controller;

import io.crnk.core.engine.dispatcher.Response;
import io.crnk.core.engine.document.Document;
import io.crnk.core.engine.http.HttpMethod;
import io.crnk.core.engine.http.HttpStatus;
import io.crnk.core.engine.internal.dispatcher.path.JsonPath;
import io.crnk.core.mock.models.Project;
import io.crnk.core.mock.repository.TaskToProjectRepository;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.utils.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldPostControllerTest extends ControllerTestBase {

    private static final String REQUEST_TYPE = HttpMethod.POST.name();

    @Test
    public void onValidRequestShouldAcceptIt() {
        // GIVEN
        JsonPath jsonPath = pathBuilder.build("tasks/1/project");
        FieldResourcePost sut = new FieldResourcePost();
        sut.init(controllerContext);

        // WHEN
        boolean result = sut.isAcceptable(jsonPath, REQUEST_TYPE);

        // THEN
        assertThat(result).isTrue();
    }

    @Test
    public void onRelationshipRequestShouldDenyIt() {
        // GIVEN
        JsonPath jsonPath = pathBuilder.build("tasks/1/relationships/project");
        FieldResourcePost sut = new FieldResourcePost();
        sut.init(controllerContext);

        // WHEN
        boolean result = sut.isAcceptable(jsonPath, REQUEST_TYPE);

        // THEN
        assertThat(result).isFalse();
    }

    @Test
    public void onNonRelationRequestShouldDenyIt() {
        // GIVEN
        JsonPath jsonPath = pathBuilder.build("tasks");
        FieldResourcePost sut = new FieldResourcePost();
        sut.init(controllerContext);

        // WHEN
        boolean result = sut.isAcceptable(jsonPath, REQUEST_TYPE);

        // THEN
        assertThat(result).isFalse();
    }

    @Test
    public void onExistingParentResourceShouldSaveIt() {
        // GIVEN
        Document newTaskDocument = new Document();
        newTaskDocument.setData(Nullable.of(createTask()));

        JsonPath taskPath = pathBuilder.build("/tasks");
        ResourcePostController resourcePost = new ResourcePostController();
        resourcePost.init(controllerContext);

        // WHEN
        Response taskResponse = resourcePost.handle(taskPath, emptyTaskQuery, newTaskDocument);

        // THEN
        assertThat(taskResponse.getDocument().getSingleData().get().getType()).isEqualTo("tasks");
        Long taskId = Long.parseLong(taskResponse.getDocument().getSingleData().get().getId());
        assertThat(taskId).isNotNull();

        /* ------- */

        // GIVEN
        Document newProjectDocument = new Document();
        newProjectDocument.setData(Nullable.of(createProject()));

        JsonPath projectPath = pathBuilder.build("/tasks/" + taskId + "/project");
        FieldResourcePost sut = new FieldResourcePost();
        sut.init(controllerContext);

        // WHEN
        Response projectResponse = sut.handle(projectPath, emptyProjectQuery, newProjectDocument);

        // THEN
        assertThat(projectResponse.getHttpStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(projectResponse.getDocument().getSingleData().get().getType()).isEqualTo("projects");
        assertThat(projectResponse.getDocument().getSingleData().get().getId()).isNotNull();
        assertThat(projectResponse.getDocument().getSingleData().get().getAttributes().get("name").asText())
                .isEqualTo("sample project");
        Long projectId = Long.parseLong(projectResponse.getDocument().getSingleData().get().getId());
        assertThat(projectId).isNotNull();

        TaskToProjectRepository taskToProjectRepository = new TaskToProjectRepository();
        Map<Long, Project> relations = taskToProjectRepository.findOneRelations(Arrays.asList(taskId), "project", new QuerySpec(Project.class));
        Assert.assertEquals(1, relations.size());
        Project project = relations.get(projectId);
        assertThat(project.getId()).isEqualTo(projectId);
    }

    @Test
    public void onExistingParentResourceShouldSaveToToMany() {
        // GIVEN
        Document newTaskDocument = new Document();
        newTaskDocument.setData(Nullable.of(createTask()));

        JsonPath taskPath = pathBuilder.build("/tasks");
        ResourcePostController resourcePost = new ResourcePostController();
        resourcePost.init(controllerContext);

        // WHEN
        Response taskResponse = resourcePost.handle(taskPath, emptyTaskQuery, newTaskDocument);

        // THEN
        assertThat(taskResponse.getDocument().getSingleData().get().getType()).isEqualTo("tasks");
        Long taskId = Long.parseLong(taskResponse.getDocument().getSingleData().get().getId());
        assertThat(taskId).isNotNull();

        /* ------- */

        // GIVEN
        Document newProjectDocument = new Document();
        newProjectDocument.setData(Nullable.of(createProject()));

        JsonPath projectPath = pathBuilder.build("/tasks/" + taskId + "/projects");
        FieldResourcePost sut = new FieldResourcePost();
        sut.init(controllerContext);

        // WHEN
        Response projectResponse = sut.handle(projectPath, emptyProjectQuery, newProjectDocument);

        // THEN
        assertThat(projectResponse.getHttpStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(projectResponse.getDocument().getSingleData().get().getType()).isEqualTo("projects");
        assertThat(projectResponse.getDocument().getSingleData().get().getId()).isNotNull();
        assertThat(projectResponse.getDocument().getSingleData().get().getAttributes().get("name").asText())
                .isEqualTo("sample project");
        Long projectId = Long.parseLong(projectResponse.getDocument().getSingleData().get().getId());
        assertThat(projectId).isNotNull();

        TaskToProjectRepository taskToProjectRepository = new TaskToProjectRepository();
        Map<Long, Project> map = taskToProjectRepository.findOneRelations(Arrays.asList(taskId), "projects", new QuerySpec(Project.class));
        Assert.assertEquals(1, map.size());
        Project project = map.get(projectId);
        assertThat(project.getId()).isEqualTo(projectId);
    }
}
