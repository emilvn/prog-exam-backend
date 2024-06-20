package dk.emilvn.exam.result;

import java.time.LocalDate;

public record ResultDTO(Long id, LocalDate date, int result, ResultType resultType, Long participantId, Long disciplineId) {
}
