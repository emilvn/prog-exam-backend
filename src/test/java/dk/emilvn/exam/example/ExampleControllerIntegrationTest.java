package dk.emilvn.exam.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ExampleControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private Long id;

    @BeforeEach
    void setUp(@Autowired ExampleRepository exampleRepository) {
        var example1 = new Example("Example1");
        var example2 = new Example("Example1");

        var savedExample = exampleRepository.save(example1);
        exampleRepository.save(example2);
        id = savedExample.getId();
    }

    @AfterEach
    void tearDown(@Autowired ExampleRepository exampleRepository) {
        exampleRepository.deleteAll();
    }

    @Test
    void givenExamples_whenGet_thenReturnAllExamples() {
        webTestClient.get()
                .uri("/examples")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExampleDTO.class)
                .hasSize(2);
    }

    @Test
    void givenExamples_whenGetWithPagination_thenReturnPagedExamples() {
        webTestClient.get()
                .uri("/examples?page=0&size=1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExampleDTO.class)
                .hasSize(1);
    }

    @Test
    void givenExampleId_whenGetWithId_thenReturnExample() {
        webTestClient.get()
                .uri("/examples/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExampleDTO.class)
                .isEqualTo(new ExampleDTO(id, "Example1"));
    }

    @Test
    void givenExampleDTO_whenPost_thenReturnCreatedExample() {
        webTestClient.post()
                .uri("/examples")
                .bodyValue(new ExampleDTO(null, "Example3"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ExampleDTO.class)
                .value(response -> {
                    assertNotNull(response);
                    assertNotNull(response.id());
                    assertEquals("Example3", response.name());
                });
    }

    @Test
    void givenExampleDTO_whenPut_thenReturnUpdatedExample() {
        webTestClient.put()
                .uri("/examples/{id}", id)
                .bodyValue(new ExampleDTO(id, "Example4"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExampleDTO.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(id, response.id());
                    assertEquals("Example4", response.name());
                });
    }

    @Test
    void givenExampleDTO_whenPatch_thenReturnPatchedExample() {
        webTestClient.patch()
                .uri("/examples/{id}", id)
                .bodyValue(new ExampleDTO(null, "Example5"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExampleDTO.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(id, response.id());
                    assertEquals("Example5", response.name());
                });
    }

    @Test
    void givenExampleId_whenDelete_thenExampleIsDeleted() {
        webTestClient.delete()
                .uri("/examples/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }
}