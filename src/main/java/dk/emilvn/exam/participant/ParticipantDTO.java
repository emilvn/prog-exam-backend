package dk.emilvn.exam.participant;

import java.time.LocalDate;

public record ParticipantDTO(Long id, String name, Boolean isMale, LocalDate birthDate, String club) {
}
