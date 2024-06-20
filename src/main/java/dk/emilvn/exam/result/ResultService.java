package dk.emilvn.exam.result;

import dk.emilvn.exam.discipline.DisciplineService;
import dk.emilvn.exam.error.NotFoundException;
import dk.emilvn.exam.participant.ParticipantService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {
    private final String cacheName = "resultCache";
    private final ResultRepository resultRepository;
    private final ParticipantService participantService;
    private final DisciplineService disciplineService;

    public ResultService(ResultRepository resultRepository, ParticipantService participantService, DisciplineService disciplineService) {
        this.resultRepository = resultRepository;
        this.participantService = participantService;
        this.disciplineService = disciplineService;
    }

    public ResultDTO toDTO(Result result) {
        return new ResultDTO(
                result.getId(),
                result.getDate(),
                result.getResult(),
                result.getResultType(),
                result.getParticipant().getId(),
                result.getDiscipline().getId()
        );
    }

    public Result fromDTO(ResultDTO resultDTO) {
        var disciplineDTO = disciplineService.findById(resultDTO.disciplineId());
        var discipline = disciplineService.fromDTO(disciplineDTO);
        var participant = participantService.findEntityById(resultDTO.participantId());

        return new Result(
                resultDTO.id(),
                resultDTO.date(),
                resultDTO.result(),
                resultDTO.resultType(),
                participant,
                discipline
        );
    }

    @Cacheable(value = cacheName, key = "'findAll'+'_'+#page+'_'+#size", unless = "#result.size() == 0")
    public List<ResultDTO> findAll(Integer page, Integer size) {
        if(page != null && size != null && page >= 0 && size > 0) {
            return resultRepository
                    .findAll(PageRequest.of(page, size))
                    .getContent()
                    .stream()
                    .map(this::toDTO)
                    .toList();
        }
        return resultRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Cacheable(value = cacheName, key = "#id", condition="#id > 0L")
    public ResultDTO findById(Long id) {
        return resultRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NotFoundException("Result not found"));
    }

    public List<ResultDTO> findByParticipantId(Long participantId) {
        return resultRepository.findAllByParticipantId(participantId).stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ResultDTO> findByDisciplineId(Long disciplineId) {
        return resultRepository.findAllByDisciplineId(disciplineId).stream()
                .map(this::toDTO)
                .toList();
    }

    @CachePut(value = cacheName, key = "#result.id()")
    @CacheEvict(value = cacheName, allEntries = true, beforeInvocation = true)
    public ResultDTO create(ResultDTO resultDTO) {
        var result = fromDTO(resultDTO);
        result = resultRepository.save(result);
        return toDTO(result);
    }

    @CachePut(value = cacheName, key = "#id")
    @CacheEvict(value = cacheName, allEntries = true, beforeInvocation = true)
    public ResultDTO update(Long id, ResultDTO resultDTO) {
        var result = resultRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Result not found"));

        var participantDTO = participantService.findById(resultDTO.participantId());
        var disciplineDTO = disciplineService.findById(resultDTO.disciplineId());
        result.setDate(resultDTO.date());
        result.setResult(resultDTO.result());
        result.setResultType(resultDTO.resultType());
        result.setParticipant(participantService.fromDTO(participantDTO));
        result.setDiscipline(disciplineService.fromDTO(disciplineDTO));
        result = resultRepository.save(result);

        return toDTO(result);
    }

    @CachePut(value = cacheName, key = "#id")
    @CacheEvict(value = cacheName, allEntries = true, beforeInvocation = true)
    public ResultDTO patch(Long id, ResultDTO resultDTO) {
        var result = resultRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Result not found"));

        if(resultDTO.date() != null) {
            result.setDate(resultDTO.date());
        }
        if(resultDTO.result() > 0) {
            result.setResult(resultDTO.result());
        }
        if(resultDTO.resultType() != null) {
            result.setResultType(resultDTO.resultType());
        }
        if(resultDTO.participantId() != null) {
            var participantDTO = participantService.findById(resultDTO.participantId());
            result.setParticipant(participantService.fromDTO(participantDTO));
        }
        if(resultDTO.disciplineId() != null) {
            var disciplineDTO = disciplineService.findById(resultDTO.disciplineId());
            result.setDiscipline(disciplineService.fromDTO(disciplineDTO));
        }
        result = resultRepository.save(result);

        return toDTO(result);
    }

    @CacheEvict(value = cacheName, allEntries = true)
    public void delete(Long id) {
        resultRepository.deleteById(id);
    }


}
