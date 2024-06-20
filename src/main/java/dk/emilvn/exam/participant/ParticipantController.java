package dk.emilvn.exam.participant;

import dk.emilvn.exam.discipline.DisciplineDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ParticipantController {
    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/participants")
    public ResponseEntity<List<ParticipantDTO>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return ResponseEntity.ok(participantService.findAll(page, size));
    }

    @GetMapping("/participants/{id}")
    public ResponseEntity<ParticipantDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(participantService.findById(id));
    }

    @PostMapping("/participants")
    public ResponseEntity<ParticipantDTO> create(@RequestBody ParticipantDTO exampleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(participantService.create(exampleDTO));
    }

    @PutMapping("/participants/{id}")
    public ResponseEntity<ParticipantDTO> update(@PathVariable Long id, @RequestBody ParticipantDTO participantDTO) {
        return ResponseEntity.ok(participantService.update(id, participantDTO));
    }

    @PatchMapping("/participants/{id}")
    public ResponseEntity<ParticipantDTO> patch(@PathVariable Long id, @RequestBody ParticipantDTO participantDTO) {
        return ResponseEntity.ok(participantService.patch(id, participantDTO));
    }

    @DeleteMapping("/participants/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        participantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/participants/{id}/disciplines")
    public ResponseEntity<List<DisciplineDTO>> getDisciplines(@PathVariable Long id) {
        return ResponseEntity.ok(participantService.getDisciplines(id));
    }

    @PutMapping("/participants/{id}/disciplines")
    public ResponseEntity<List<DisciplineDTO>> addDiscipline(@PathVariable Long id, @RequestBody DisciplineDTO disciplineDTO) {
        return ResponseEntity.ok(participantService.addDiscipline(id, disciplineDTO.id()));
    }

    @DeleteMapping("/participants/{id}/disciplines/{disciplineId}")
    public ResponseEntity<Void> removeDiscipline(@PathVariable Long id, @PathVariable Long disciplineId) {
        participantService.removeDiscipline(id, disciplineId);
        return ResponseEntity.noContent().build();
    }
}
