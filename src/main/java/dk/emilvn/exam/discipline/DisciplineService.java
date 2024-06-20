package dk.emilvn.exam.discipline;

import dk.emilvn.exam.error.NotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineService {
    private final String cacheName = "disciplineCache";
    private final DisciplineRepository disciplineRepository;

    public DisciplineService(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    public DisciplineDTO toDTO(Discipline discipline) {
        return new DisciplineDTO(
                discipline.getId(),
                discipline.getName(),
                discipline.getResultType()
        );
    }

    public Discipline fromDTO(DisciplineDTO disciplineDTO) {
        return new Discipline(
                disciplineDTO.id(),
                disciplineDTO.name(),
                disciplineDTO.resultType()
        );
    }

    @Cacheable(value = cacheName, key = "'findAll'", unless = "#result.size() == 0")
    public List<DisciplineDTO> findAll() {
        return disciplineRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public DisciplineDTO findById(Long id) {
        return disciplineRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NotFoundException("Discipline not found"));
    }

    @CachePut(value = cacheName, key = "#result.id")
    @CacheEvict(value = cacheName, key = "'findAll'", beforeInvocation = true)
    public DisciplineDTO create(DisciplineDTO disciplineDTO) {
        var discipline = fromDTO(disciplineDTO);
        return toDTO(disciplineRepository.save(discipline));
    }

    @CachePut(value = cacheName, key = "#id")
    @CacheEvict(value = cacheName, key = "'findAll'", beforeInvocation = true)
    public DisciplineDTO update(Long id, DisciplineDTO disciplineDTO) {
        var discipline = disciplineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Discipline not found"));

        discipline.setName(disciplineDTO.name());
        discipline.setResultType(disciplineDTO.resultType());

        return toDTO(disciplineRepository.save(discipline));
    }

    @CacheEvict(value = cacheName, allEntries = true)
    public void delete(Long id) {
        if(disciplineRepository.existsById(id)) {
            disciplineRepository.deleteById(id);
        } else {
            throw new NotFoundException("Discipline not found");
        }
    }
}
