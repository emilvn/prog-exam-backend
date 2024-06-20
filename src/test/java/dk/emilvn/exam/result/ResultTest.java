package dk.emilvn.exam.result;

import dk.emilvn.exam.discipline.Discipline;
import dk.emilvn.exam.participant.Participant;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void givenValidResult_whenCreatingResult_thenResultIsCreated() {
        var participant = new Participant(1L, "John Doe", true, LocalDate.of(1996, 1, 1), "Club");
        var discipline = new Discipline(1L, "Discipline", ResultType.TIME_IN_MILLISECONDS);
        participant.addDiscipline(discipline);

        var result = new Result(1L, LocalDate.now(), 100, ResultType.TIME_IN_MILLISECONDS, participant, discipline);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDate.now(), result.getDate());
        assertEquals(100, result.getResult());
        assertEquals(ResultType.TIME_IN_MILLISECONDS, result.getResultType());
        assertEquals(participant, result.getParticipant());
        assertEquals(discipline, result.getDiscipline());
    }

    @Test
    void givenInvalidDiscipline_whenCreatingResult_thenIllegalArgumentExceptionIsThrown() {
        var participant = new Participant(1L, "John Doe", true, LocalDate.of(1996, 1, 1), "Club");
        var discipline = new Discipline(1L, "Discipline", ResultType.TIME_IN_MILLISECONDS);

        assertThrows(IllegalArgumentException.class, () -> new Result(1L, LocalDate.now(), 100, ResultType.TIME_IN_MILLISECONDS, participant, discipline));
    }

    @Test
    void givenInvalidResultType_whenCreatingResult_thenIllegalArgumentExceptionIsThrown() {
        var participant = new Participant(1L, "John Doe", true, LocalDate.of(1996, 1, 1), "Club");
        var discipline = new Discipline(1L, "Discipline", ResultType.TIME_IN_MILLISECONDS);
        participant.addDiscipline(discipline);

        assertThrows(IllegalArgumentException.class, () -> new Result(1L, LocalDate.now(), 100, ResultType.DISTANCE_IN_CENTIMETRES, participant, discipline));
    }

}