package dk.emilvn.exam.participant;

import dk.emilvn.exam.discipline.DisciplineDTO;
import dk.emilvn.exam.discipline.DisciplineService;
import dk.emilvn.exam.error.NotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {
    private final String cacheName = "participantCache";
    private final ParticipantRepository participantRepository;
    private final DisciplineService disciplineService;

    public ParticipantService(ParticipantRepository participantRepository, DisciplineService disciplineService) {
        this.participantRepository = participantRepository;
        this.disciplineService = disciplineService;
    }

    public ParticipantDTO toDTO(Participant participant) {
        return new ParticipantDTO(
                participant.getId(),
                participant.getName(),
                participant.isMale(),
                participant.getBirthDate(),
                participant.getClub()
        );
    }

    public Participant fromDTO(ParticipantDTO participantDTO) {
        return new Participant(
                participantDTO.id(),
                participantDTO.name(),
                participantDTO.isMale(),
                participantDTO.birthDate(),
                participantDTO.club()
        );
    }

    @Cacheable(value = cacheName, key = "'findAll'+'_'+#page+'_'+#size", unless = "#result.size() == 0")
    public List<ParticipantDTO> findAll(Integer page, Integer size) {
        if(page != null && size != null && page >= 0 && size > 0) {
            return participantRepository
                    .findAll(PageRequest.of(page, size))
                    .getContent()
                    .stream()
                    .map(this::toDTO)
                    .toList();
        }
        return participantRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Cacheable(value = cacheName, key = "#id", condition="#id > 0L")
    public ParticipantDTO findById(Long id) {
        return participantRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NotFoundException("Participant not found"));
    }

    public Participant findEntityById(Long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Participant not found"));
    }

    @CachePut(value = cacheName, key = "#result.id()")
    @CacheEvict(value = cacheName, allEntries = true, beforeInvocation = true)
    public ParticipantDTO create(ParticipantDTO exampleDTO) {
        var example = fromDTO(exampleDTO);
        var savedExample = participantRepository.save(example);

        return toDTO(savedExample);
    }

    @CachePut(value = cacheName, key = "#id")
    @CacheEvict(value = cacheName, allEntries = true, beforeInvocation = true)
    public ParticipantDTO update(Long id, ParticipantDTO exampleDTO) {
        var example = participantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Participant not found"));

        example.setName(exampleDTO.name());
        example.setMale(exampleDTO.isMale());
        example.setBirthDate(exampleDTO.birthDate());
        example.setClub(exampleDTO.club());

        participantRepository.save(example);

        return toDTO(example);
    }

    @CachePut(value = cacheName, key = "#id")
    @CacheEvict(value = cacheName, allEntries = true, beforeInvocation = true)
    public ParticipantDTO patch(Long id, ParticipantDTO exampleDTO) {
        var example = participantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Participant not found"));

        if(exampleDTO.name() != null) {
            example.setName(exampleDTO.name());
        }
        if(exampleDTO.isMale() != null) {
            example.setMale(exampleDTO.isMale());
        }
        if(exampleDTO.birthDate() != null) {
            example.setBirthDate(exampleDTO.birthDate());
        }
        if(exampleDTO.club() != null) {
            example.setClub(exampleDTO.club());
        }

        participantRepository.save(example);

        return toDTO(example);
    }

    @CacheEvict(value = cacheName, allEntries = true)
    public void delete(Long id) {
        if(!participantRepository.existsById(id)) {
            throw new NotFoundException("Participant not found");
        }
        participantRepository.deleteById(id);
    }

    public List<DisciplineDTO> getDisciplines(Long id) {
        var participant = participantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Participant not found"));

        return participant.getDisciplines().stream()
                .map(disciplineService::toDTO)
                .toList();
    }

    public List<DisciplineDTO> addDiscipline(Long id, Long disciplineId) {
        var participant = participantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Participant not found"));

        var disciplineDTO = disciplineService.findById(disciplineId);
        var discipline = disciplineService.fromDTO(disciplineDTO);

        participant.addDiscipline(discipline);
        participantRepository.save(participant);

        return participant.getDisciplines().stream()
                .map(disciplineService::toDTO)
                .toList();
    }

    public List<DisciplineDTO> removeDiscipline(Long id, Long disciplineId) {
        var participant = participantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Participant not found"));

        var disciplineDTO = disciplineService.findById(disciplineId);
        var discipline = disciplineService.fromDTO(disciplineDTO);

        participant.removeDiscipline(discipline);
        participantRepository.save(participant);

        return participant.getDisciplines().stream()
                .map(disciplineService::toDTO)
                .toList();
    }
}
