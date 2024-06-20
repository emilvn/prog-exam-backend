package dk.emilvn.exam.result;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ResultController {
    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/results")
    public ResponseEntity<List<ResultDTO>> getAll(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(resultService.findAll(page, size));
    }

    @GetMapping("/results/{id}")
    public ResponseEntity<ResultDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(resultService.findById(id));
    }

    @GetMapping("/results/participant/{participantId}")
    public ResponseEntity<List<ResultDTO>> getByParticipantId(@PathVariable Long participantId) {
        return ResponseEntity.ok(resultService.findByParticipantId(participantId));
    }

    @GetMapping("/results/discipline/{disciplineId}")
    public ResponseEntity<List<ResultDTO>> getByDisciplineId(@PathVariable Long disciplineId) {
        return ResponseEntity.ok(resultService.findByDisciplineId(disciplineId));
    }

    @PostMapping("/results")
    public ResponseEntity<ResultDTO> create(@RequestBody ResultDTO resultDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resultService.create(resultDTO));
    }

    @PutMapping("/results/{id}")
    public ResponseEntity<ResultDTO> update(@PathVariable Long id, @RequestBody ResultDTO resultDTO) {
        return ResponseEntity.ok(resultService.update(id, resultDTO));
    }

    @PatchMapping("/results/{id}")
    public ResponseEntity<ResultDTO> patch(@PathVariable Long id, @RequestBody ResultDTO resultDTO) {
        return ResponseEntity.ok(resultService.patch(id, resultDTO));
    }

    @DeleteMapping("/results/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resultService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
