package dk.emilvn.exam.result;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findAllByParticipantId(Long participantId);
    List<Result> findAllByDisciplineId(Long disciplineId);
}
