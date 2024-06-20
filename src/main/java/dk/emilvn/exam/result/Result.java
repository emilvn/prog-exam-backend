package dk.emilvn.exam.result;


import dk.emilvn.exam.discipline.Discipline;
import dk.emilvn.exam.participant.Participant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private int result;

    @Enumerated(EnumType.STRING)
    private ResultType resultType;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Participant participant;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Discipline discipline;

    public Result(Long id, LocalDate date, int result, ResultType resultType, Participant participant, Discipline discipline) {
        this(date, result, resultType, participant, discipline);
        this.id = id;
    }

    public Result(LocalDate date, int result, ResultType resultType, Participant participant, Discipline discipline) {
        validate(date, result, resultType, participant, discipline);
        this.date = date;
        this.result = result;
        this.resultType = resultType;
        this.participant = participant;
        this.discipline = discipline;
    }

    public void validate(LocalDate date, double result, ResultType resultType, Participant participant, Discipline discipline) {
        if (date == null || result < 0 || participant == null || discipline == null) {
            throw new IllegalArgumentException("Invalid result");
        }
        if(!participant.hasDiscipline(discipline)) {
            throw new IllegalArgumentException("Participant does not have discipline");
        }
        if(!resultType.equals(discipline.getResultType())) {
            throw new IllegalArgumentException("Result type does not match discipline");
        }
    }
}
