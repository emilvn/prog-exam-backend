package dk.emilvn.exam.discipline;

import dk.emilvn.exam.result.ResultType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Discipline {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private ResultType resultType;

    public Discipline(String name, ResultType resultType) {
        this.name = name;
        this.resultType = resultType;
    }
}
