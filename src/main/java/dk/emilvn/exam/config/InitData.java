package dk.emilvn.exam.config;

import dk.emilvn.exam.example.ExampleDTO;
import dk.emilvn.exam.example.ExampleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class InitData implements CommandLineRunner {

    private final ExampleService exampleService;

    public InitData(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @Override
    public void run(String... args) throws Exception {
        createExampleData();
    }

    private void createExampleData() {
        exampleService.create(new ExampleDTO(null, "Example 1"));
    }
}
