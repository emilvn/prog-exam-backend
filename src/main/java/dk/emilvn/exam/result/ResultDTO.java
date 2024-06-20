package dk.emilvn.exam.result;

import java.time.LocalDate;

public record ResultDTO(Long id, LocalDate date, double result, ResultType resultType, Long participantId, Long disciplineId) {
}
