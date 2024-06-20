package dk.emilvn.exam.discipline;

import dk.emilvn.exam.result.ResultType;

public record DisciplineDTO(Long id, String name, ResultType resultType) {
}
