package dk.emilvn.exam.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExampleController {
    private final ExampleService exampleService;

    public ExampleController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping("/examples")
    public ResponseEntity<List<ExampleDTO>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return ResponseEntity.ok(exampleService.findAll(page, size));
    }

    @GetMapping("/examples/{id}")
    public ResponseEntity<ExampleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(exampleService.findById(id));
    }

    @PostMapping("/examples")
    public ResponseEntity<ExampleDTO> create(@RequestBody ExampleDTO exampleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exampleService.create(exampleDTO));
    }

    @PutMapping("/examples/{id}")
    public ResponseEntity<ExampleDTO> update(@PathVariable Long id, @RequestBody ExampleDTO exampleDTO) {
        return ResponseEntity.ok(exampleService.update(id, exampleDTO));
    }

    @PatchMapping("/examples/{id}")
    public ResponseEntity<ExampleDTO> patch(@PathVariable Long id, @RequestBody ExampleDTO exampleDTO) {
        return ResponseEntity.ok(exampleService.patch(id, exampleDTO));
    }

    @DeleteMapping("/examples/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exampleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
