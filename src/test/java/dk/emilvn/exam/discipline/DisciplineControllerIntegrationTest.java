package dk.emilvn.exam.discipline;

import dk.emilvn.exam.result.ResultType;
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
class DisciplineControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private Long disciplineId;

    @BeforeEach
    void setUp(@Autowired DisciplineRepository disciplineRepository) {
        var discipline1 = new Discipline("Discipline1", ResultType.TIME_IN_SECONDS);
        var discipline2 = new Discipline("Discipline2", ResultType.DISTANCE_IN_METRES);
        var savedDiscipline = disciplineRepository.save(discipline1);
        disciplineRepository.save(discipline2);
        disciplineId = savedDiscipline.getId();
    }

    @AfterEach
    void tearDown(@Autowired DisciplineRepository disciplineRepository) {
        disciplineRepository.deleteAll();
    }

    @Test
    void whenGetAll_thenReturnListOfAllDisciplines() {
        webTestClient.get()
                .uri("/disciplines")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DisciplineDTO.class)
                .hasSize(2);
    }

    @Test
    void givenDisciplineId_whenGetById_thenReturnDiscipline() {
        webTestClient.get()
                .uri("/disciplines/{id}", disciplineId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DisciplineDTO.class)
                .value(disciplineDTO -> {
                    assertEquals(disciplineId, disciplineDTO.id());
                    assertEquals("Discipline1", disciplineDTO.name());
                    assertEquals(ResultType.TIME_IN_SECONDS, disciplineDTO.resultType());
                });
    }

    @Test
    void givenDisciplineDTO_whenCreate_thenReturnCreatedDiscipline() {
        var disciplineDTO = new DisciplineDTO(null, "Discipline3", ResultType.TIME_IN_SECONDS);
        webTestClient.post()
                .uri("/disciplines")
                .bodyValue(disciplineDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(DisciplineDTO.class)
                .value(createdDisciplineDTO -> {
                    assertNotNull(createdDisciplineDTO.id());
                    assertEquals("Discipline3", createdDisciplineDTO.name());
                    assertEquals(ResultType.TIME_IN_SECONDS, createdDisciplineDTO.resultType());
                });
    }

    @Test
    void givenDisciplineIdAndDisciplineDTO_whenUpdate_thenReturnUpdatedDiscipline() {
        var disciplineDTO = new DisciplineDTO(null, "Discipline3", ResultType.TIME_IN_SECONDS);
        webTestClient.put()
                .uri("/disciplines/{id}", disciplineId)
                .bodyValue(disciplineDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DisciplineDTO.class)
                .value(updatedDisciplineDTO -> {
                    assertEquals(disciplineId, updatedDisciplineDTO.id());
                    assertEquals("Discipline3", updatedDisciplineDTO.name());
                    assertEquals(ResultType.TIME_IN_SECONDS, updatedDisciplineDTO.resultType());
                });
    }

    @Test
    void givenDisciplineId_whenDelete_thenDisciplineIsDeleted() {
        webTestClient.delete()
                .uri("/disciplines/{id}", disciplineId)
                .exchange()
                .expectStatus().isNoContent();
    }
}