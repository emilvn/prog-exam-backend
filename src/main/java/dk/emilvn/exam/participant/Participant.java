package dk.emilvn.exam.participant;

import dk.emilvn.exam.discipline.Discipline;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean isMale;
    private LocalDate birthDate;
    private String club;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<Discipline> disciplines = new ArrayList<>();

    public Participant(Long id, String name, boolean isMale, LocalDate birthDate, String club) {
        this.id = id;
        this.name = name;
        this.isMale = isMale;
        this.birthDate = birthDate;
        this.club = club;
    }

    public Participant(String name, boolean isMale, LocalDate birthDate, String club) {
        this.name = name;
        this.isMale = isMale;
        this.birthDate = birthDate;
        this.club = club;
    }

    public void addDiscipline(Discipline discipline) {
        if(!hasDiscipline(discipline)){
            disciplines.add(discipline);
        }
    }

    public void removeDiscipline(Discipline discipline) {
        disciplines.removeIf(d -> d.getId().equals(discipline.getId()));
    }

    public boolean hasDiscipline(Discipline discipline) {
        return disciplines.stream().anyMatch(d -> d.getId().equals(discipline.getId()));
    }
}
