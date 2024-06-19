package dk.emilvn.exam.example;

import dk.emilvn.exam.error.NotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExampleService {
    private final String cacheName = "exampleCache";
    private final ExampleRepository exampleRepository;

    public ExampleService(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    public ExampleDTO toDTO(Example example) {
        return new ExampleDTO(
                example.getId(),
                example.getName()
        );
    }

    public Example fromDTO(ExampleDTO exampleDTO) {
        return new Example(
                exampleDTO.name()
        );
    }

    @Cacheable(value = cacheName, key = "'findAll'+'_'+#page+'_'+#size", unless = "#result.size() == 0")
    public List<ExampleDTO> findAll(Integer page, Integer size) {
        if(page != null && size != null && page >= 0 && size > 0) {
            return exampleRepository
                    .findAll(PageRequest.of(page, size))
                    .getContent()
                    .stream()
                    .map(this::toDTO)
                    .toList();
        }
        return exampleRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Cacheable(value = cacheName, key = "#id", condition="#id > 0L")
    public ExampleDTO findById(Long id) {
        return exampleRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NotFoundException("Example not found"));
    }

    @CachePut(value = cacheName, key = "#result.id()")
    @CacheEvict(value = cacheName, allEntries = true, beforeInvocation = true)
    public ExampleDTO create(ExampleDTO exampleDTO) {
        var example = fromDTO(exampleDTO);
        var savedExample = exampleRepository.save(example);

        return toDTO(savedExample);
    }

    @CachePut(value = cacheName, key = "#id")
    @CacheEvict(value = cacheName, allEntries = true, beforeInvocation = true)
    public ExampleDTO update(Long id, ExampleDTO exampleDTO) {
        var example = exampleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Example not found"));

        example.setName(exampleDTO.name());

        exampleRepository.save(example);

        return toDTO(example);
    }

    @CachePut(value = cacheName, key = "#id")
    @CacheEvict(value = cacheName, allEntries = true, beforeInvocation = true)
    public ExampleDTO patch(Long id, ExampleDTO exampleDTO) {
        var example = exampleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Example not found"));

        if(exampleDTO.name() != null) {
            example.setName(exampleDTO.name());
        }

        exampleRepository.save(example);

        return toDTO(example);
    }

    @CacheEvict(value = cacheName, allEntries = true)
    public void delete(Long id) {
        if(!exampleRepository.existsById(id)) {
            throw new NotFoundException("Example not found");
        }
        exampleRepository.deleteById(id);
    }
}
