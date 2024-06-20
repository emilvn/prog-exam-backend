package dk.emilvn.exam.discipline;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DisciplineController {
    private final DisciplineService disciplineService;

    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    @GetMapping("/disciplines")
    public ResponseEntity<List<DisciplineDTO>> getAll() {
        return ResponseEntity.ok(disciplineService.findAll());
    }

    @GetMapping("/disciplines/{id}")
    public ResponseEntity<DisciplineDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(disciplineService.findById(id));
    }

    @PostMapping("/disciplines")
    public ResponseEntity<DisciplineDTO> create(@RequestBody DisciplineDTO disciplineDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(disciplineService.create(disciplineDTO));
    }

    @PutMapping("/disciplines/{id}")
    public ResponseEntity<DisciplineDTO> update(@PathVariable Long id, @RequestBody DisciplineDTO disciplineDTO) {
        return ResponseEntity.ok(disciplineService.update(id, disciplineDTO));
    }

    @DeleteMapping("/disciplines/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        disciplineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
