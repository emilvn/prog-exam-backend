package dk.emilvn.exam.result;

import dk.emilvn.exam.discipline.Discipline;
import dk.emilvn.exam.discipline.DisciplineRepository;
import dk.emilvn.exam.participant.Participant;
import dk.emilvn.exam.participant.ParticipantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ResultControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private Long participantId;
    private Long disciplineId;
    private Long resultId;

    @BeforeEach
    void setUp(@Autowired ParticipantRepository participantRepository, @Autowired DisciplineRepository disciplineRepository, @Autowired ResultRepository resultRepository) {
        var discipline = new Discipline("Discipline", ResultType.TIME_IN_SECONDS);
        var savedDiscipline = disciplineRepository.save(discipline);
        disciplineId = savedDiscipline.getId();

        var participant1 = new Participant("Participant1", true, LocalDate.of(1996, 1, 1), "Penguin");
        var participant2 = new Participant("Participant2", true, LocalDate.of(1996, 1, 1), "Penguin");
        participant1.addDiscipline(discipline);
        var savedParticipant = participantRepository.save(participant1);
        participantRepository.save(participant2);
        participantId = savedParticipant.getId();

        var result1 = new Result(LocalDate.now(), 10.0, ResultType.TIME_IN_SECONDS, savedParticipant, savedDiscipline);
        var result2 = new Result(LocalDate.now(), 10.0, ResultType.TIME_IN_SECONDS, savedParticipant, savedDiscipline);
        var savedResult = resultRepository.save(result1);
        resultRepository.save(result2);
        resultId = savedResult.getId();
    }

    @AfterEach
    void tearDown(@Autowired ParticipantRepository participantRepository, @Autowired DisciplineRepository disciplineRepository, @Autowired ResultRepository resultRepository) {
        resultRepository.deleteAll();
        participantRepository.deleteAll();
        disciplineRepository.deleteAll();
    }

    @Test
    void givenNoPaginationQueryParams_whenGetAll_thenReturnListOfAllResults() {
        webTestClient.get()
                .uri("/results")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ResultDTO.class)
                .hasSize(2);
    }

    @Test
    void givenPaginationQueryParams_whenGetAll_thenReturnPagedResults() {
        webTestClient.get()
                .uri("/results?page=0&size=1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ResultDTO.class)
                .hasSize(1);
    }

    @Test
    void givenResultId_whenGetWithId_thenReturnResult() {
        webTestClient.get()
                .uri("/results/{id}", resultId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResultDTO.class)
                .isEqualTo(new ResultDTO(resultId, LocalDate.now(), 10.0, ResultType.TIME_IN_SECONDS, participantId, disciplineId));
    }

    @Test
    void givenParticipantId_whenGetByParticipantId_thenReturnResults() {
        webTestClient.get()
                .uri("/results/participant/{id}", participantId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ResultDTO.class)
                .hasSize(2);
    }

    @Test
    void givenDisciplineId_whenGetByDisciplineId_thenReturnResults() {
        webTestClient.get()
                .uri("/results/discipline/{id}", disciplineId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ResultDTO.class)
                .hasSize(2);
    }

    @Test
    void givenResultDTO_whenPost_thenReturnCreatedResult() {
        var resultDTO = new ResultDTO(null, LocalDate.now(), 10.0, ResultType.TIME_IN_SECONDS, participantId, disciplineId);

        webTestClient.post()
                .uri("/results")
                .bodyValue(resultDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ResultDTO.class)
                .isEqualTo(new ResultDTO(resultId + 2, LocalDate.now(), 10.0, ResultType.TIME_IN_SECONDS, participantId, disciplineId));
    }

    @Test
    void givenResultDTO_whenPut_thenReturnUpdatedResult() {
        var resultDTO = new ResultDTO(resultId, LocalDate.now(), 20.0, ResultType.TIME_IN_SECONDS, participantId, disciplineId);

        webTestClient.put()
                .uri("/results/{id}", resultId)
                .bodyValue(resultDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResultDTO.class)
                .isEqualTo(resultDTO);
    }

    @Test
    void givenResultDTO_whenPatch_thenReturnPartiallyUpdatedResult() {
        var resultDTO = new ResultDTO(resultId, null, 33.0, null, null, null);

        webTestClient.patch()
                .uri("/results/{id}", resultId)
                .bodyValue(resultDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResultDTO.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(resultId, response.id());
                    assertEquals(LocalDate.now(), response.date());
                    assertEquals(33.0, response.result());
                    assertEquals(ResultType.TIME_IN_SECONDS, response.resultType());
                    assertEquals(participantId, response.participantId());
                    assertEquals(disciplineId, response.disciplineId());
                });
    }

    @Test
    void givenResultId_whenDelete_thenResultIsDeleted() {
        webTestClient.delete()
                .uri("/results/{id}", resultId)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/results/{id}", resultId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void givenInvalidParticipantId_whenPost_thenNotFound() {
        var resultDTO = new ResultDTO(null, LocalDate.now(), 10.0, ResultType.TIME_IN_SECONDS, 0L, disciplineId);

        webTestClient.post()
                .uri("/results")
                .bodyValue(resultDTO)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void givenParticipantIdWithWrongDiscipline_whenPost_thenBadRequest() {
        var resultDTO = new ResultDTO(null, LocalDate.now(), 10.0, ResultType.TIME_IN_SECONDS, participantId+1, disciplineId);

        webTestClient.post()
                .uri("/results")
                .bodyValue(resultDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }
}