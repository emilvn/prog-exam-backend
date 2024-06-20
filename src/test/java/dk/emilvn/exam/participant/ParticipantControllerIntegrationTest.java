package dk.emilvn.exam.participant;

import dk.emilvn.exam.discipline.Discipline;
import dk.emilvn.exam.discipline.DisciplineDTO;
import dk.emilvn.exam.discipline.DisciplineRepository;
import dk.emilvn.exam.result.ResultType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ParticipantControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private Long participantId;
    private Long disciplineId;

    @BeforeEach
    void setUp(@Autowired ParticipantRepository participantRepository, @Autowired DisciplineRepository disciplineRepository) {
        var participant1 = new Participant("Participant1", true, LocalDate.of(1996, 1, 1), "Penguin");
        var participant2 = new Participant("Participant2", true, LocalDate.of(1996, 1, 1), "Penguin");
        var discipline = new Discipline("Discipline", ResultType.TIME_IN_SECONDS);

        var savedParticipant = participantRepository.save(participant1);
        participantRepository.save(participant2);
        var savedDiscipline = disciplineRepository.save(discipline);
        participantId = savedParticipant.getId();
        disciplineId = savedDiscipline.getId();
    }

    @AfterEach
    void tearDown(@Autowired ParticipantRepository participantRepository, @Autowired DisciplineRepository disciplineRepository) {
        participantRepository.deleteAll();
        disciplineRepository.deleteAll();
    }

    @Test
    void givenNoPaginationQueryParams_whenGetAll_thenReturnAllParticipants() {
        webTestClient.get()
                .uri("/participants")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ParticipantDTO.class)
                .hasSize(2);
    }

    @Test
    void givenPaginationQueryParams_whenGetAll_thenReturnPagedParticipants() {
        webTestClient.get()
                .uri("/participants?page=0&size=1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ParticipantDTO.class)
                .hasSize(1);
    }

    @Test
    void givenParticipantId_whenGetWithId_thenReturnParticipant() {
        webTestClient.get()
                .uri("/participants/{id}", participantId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ParticipantDTO.class)
                .isEqualTo(new ParticipantDTO(participantId, "Participant1", true, LocalDate.of(1996, 1, 1), "Penguin"));
    }

    @Test
    void givenParticipantDTO_whenPost_thenReturnCreatedParticipant() {
        webTestClient.post()
                .uri("/participants")
                .bodyValue(new ParticipantDTO(null, "Participant3", true, LocalDate.of(1996, 1, 1), "Penguin"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ParticipantDTO.class)
                .value(response -> {
                    assertNotNull(response);
                    assertNotNull(response.id());
                    assertEquals("Participant3", response.name());
                    assertEquals(true, response.isMale());
                    assertEquals(LocalDate.of(1996, 1, 1), response.birthDate());
                    assertEquals("Penguin", response.club());
                });
    }

    @Test
    void givenParticipantDTO_whenPut_thenReturnUpdatedParticipant() {
        webTestClient.put()
                .uri("/participants/{id}", participantId)
                .bodyValue(new ParticipantDTO(participantId, "Participant4", true, LocalDate.of(1996, 1, 1), "Penguin"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ParticipantDTO.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(participantId, response.id());
                    assertEquals("Participant4", response.name());
                    assertEquals(true, response.isMale());
                    assertEquals(LocalDate.of(1996, 1, 1), response.birthDate());
                    assertEquals("Penguin", response.club());
                });
    }

    @Test
    void givenParticipantDTO_whenPatch_thenReturnPatchedParticipant() {
        webTestClient.patch()
                .uri("/participants/{id}", participantId)
                .bodyValue(new ParticipantDTO(null, "Participant5", null, null, null))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ParticipantDTO.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(participantId, response.id());
                    assertEquals("Participant5", response.name());
                    assertEquals(true, response.isMale());
                    assertEquals(LocalDate.of(1996, 1, 1), response.birthDate());
                    assertEquals("Penguin", response.club());
                });
    }

    @Test
    void givenParticipantId_whenDelete_thenParticipantIsDeleted() {
        webTestClient.delete()
                .uri("/participants/{id}", participantId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void givenParticipantId_whenGetDisciplines_thenReturnListOfParticipantDisciplines() {
        webTestClient.get()
                .uri("/participants/{id}/disciplines", participantId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DisciplineDTO.class)
                .value(response -> {
                    assertEquals(0, response.size());
                });
    }

    @Test
    void givenDisciplineDTO_whenAddDiscipline_thenReturnListOfParticipantDisciplines() {
        webTestClient.put()
                .uri("/participants/{id}/disciplines", participantId)
                .bodyValue(new DisciplineDTO(disciplineId, "Discipline", ResultType.TIME_IN_SECONDS))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DisciplineDTO.class)
                .value(response -> {
                    assertEquals(1, response.size());
                    assertEquals(disciplineId, response.getFirst().id());
                    assertEquals("Discipline", response.getFirst().name());
                });
    }

    @Test
    void givenParticipantIdAndDisciplineId_whenRemoveDiscipline_thenReturnListOfParticipantDisciplines() {
        webTestClient.delete()
                .uri("/participants/{id}/disciplines/{disciplineId}", participantId, disciplineId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DisciplineDTO.class)
                .value(response -> {
                    assertEquals(0, response.size());
                });
    }


}