package dk.emilvn.exam.participant;

import dk.emilvn.exam.config.CacheConfig;
import dk.emilvn.exam.discipline.DisciplineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CacheConfig.class, ParticipantService.class})
@EnableCaching
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class,
        RedisAutoConfiguration.class
})
class ParticipantServiceCachingIntegrationTest {

    @MockBean
    private ParticipantRepository mockParticipantRepository;

    // ParticipantService depends on DisciplineService
    @MockBean
    private DisciplineService disciplineService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void clearCache() {
        Objects.requireNonNull(cacheManager.getCache("participantCache")).clear();
    }

    @Test
    void givenCaching_whenFindParticipantById_thenParticipantReturnedFromCache() {
        var cache = Objects.requireNonNull(cacheManager.getCache("participantCache"));
        var aParticipant = new Participant(1L, "Participant1", true, LocalDate.of(1996, 1, 1), "Penguin");
        given(mockParticipantRepository.findById(1L)).willReturn(Optional.of(aParticipant));

        var participantCacheMiss = participantService.findById(1L);
        var participantCacheHit = participantService.findById(1L);

        assertThat(participantCacheMiss).isEqualTo(participantService.toDTO(aParticipant));
        assertThat(participantCacheHit).isEqualTo(participantService.toDTO(aParticipant));

        verify(mockParticipantRepository, times(1)).findById(1L);
        assertThat(cache.get(1L, ParticipantDTO.class)).isEqualTo(participantService.toDTO(aParticipant));
    }

    @Test
    void givenCaching_whenFindAll_thenParticipantsReturnedFromCache() {
        var cache = Objects.requireNonNull(cacheManager.getCache("participantCache"));
        var aParticipant = new Participant(1L, "Participant1", true, LocalDate.of(1996, 1, 1), "Penguin");
        var anotherParticipant = new Participant(2L, "Participant2", true, LocalDate.of(1996, 1, 1), "Penguin");
        var participants = List.of(aParticipant, anotherParticipant);
        given(mockParticipantRepository.findAll()).willReturn(participants);

        var participantsCacheMiss = participantService.findAll(null, null);
        var participantsCacheHit = participantService.findAll(null, null);

        assertThat(participantsCacheMiss).isEqualTo(List.of(participantService.toDTO(aParticipant), participantService.toDTO(anotherParticipant)));
        assertThat(participantsCacheHit).isEqualTo(List.of(participantService.toDTO(aParticipant), participantService.toDTO(anotherParticipant)));

        verify(mockParticipantRepository, times(1)).findAll();
        assertThat(Objects.requireNonNull(cache.get("findAll_null_null", List.class)).size()).isEqualTo(2);
    }

    @Test
    void givenCaching_whenCreate_thenparticipantCached() {
        var cache = Objects.requireNonNull(cacheManager.getCache("participantCache"));
        var aParticipant = new Participant(1L, "Participant1", true, LocalDate.of(1996, 1, 1), "Penguin");
        given(mockParticipantRepository.save(any(Participant.class))).willReturn(aParticipant);
        var participantDTO = new ParticipantDTO(null, "Participant1", true, LocalDate.of(1996, 1, 1), "Penguin");

        var createdParticipant = participantService.create(participantDTO);

        assertThat(createdParticipant).isEqualTo(participantService.toDTO(aParticipant));
        assertThat(cache.get(createdParticipant.id(), ParticipantDTO.class)).isEqualTo(createdParticipant);
    }

    @Test
    void givenCaching_whenUpdate_thenParticipantUpdatedInCache() {
        var cache = Objects.requireNonNull(cacheManager.getCache("participantCache"));
        var anParticipant = new Participant(1L, "Participant1", true, LocalDate.of(1996, 1, 1), "Penguin");
        given(mockParticipantRepository.findById(1L)).willReturn(Optional.of(anParticipant));

        var updatedParticipant = new ParticipantDTO(1L, "UpdatedName", true, LocalDate.of(1996, 1, 1), "Penguin");
        var updatedResult = participantService.update(1L, updatedParticipant);

        assertThat(updatedResult).isEqualTo(updatedParticipant);
        assertThat(cache.get(1L, ParticipantDTO.class)).isEqualTo(updatedParticipant);
    }

    @Test
    void givenCaching_whenPatch_thenParticipantPatchedInCache() {
        var cache = Objects.requireNonNull(cacheManager.getCache("participantCache"));
        var anParticipant = new Participant(1L, "Participant1", true, LocalDate.of(1996, 1, 1), "Penguin");
        given(mockParticipantRepository.findById(1L)).willReturn(Optional.of(anParticipant));

        var patchedParticipant = new ParticipantDTO(1L, "PatchedName", null, null, null);
        var patchedResult = participantService.patch(1L, patchedParticipant);

        assertThat(patchedResult).isEqualTo(new ParticipantDTO(1L, "PatchedName", true, LocalDate.of(1996, 1, 1), "Penguin"));
        assertThat(cache.get(1L, ParticipantDTO.class)).isEqualTo(new ParticipantDTO(1L, "PatchedName", true, LocalDate.of(1996, 1, 1), "Penguin"));

    }

    @Test
    void givenCaching_whenDelete_thenParticipantRemovedFromCache() {
        var cache = Objects.requireNonNull(cacheManager.getCache("participantCache"));
        given(mockParticipantRepository.existsById(1L)).willReturn(true);

        participantService.delete(1L);

        verify(mockParticipantRepository, times(1)).deleteById(1L);
        assertThat(cache.get(1L)).isNull();
    }

    @Test
    void givenCaching_whenDelete_thenCacheIsCleared(){
        var cache = Objects.requireNonNull(cacheManager.getCache("participantCache"));
        var anParticipant = new Participant(1L, "Participant1", true, LocalDate.of(1996, 1, 1), "Penguin");
        given(mockParticipantRepository.findById(1L)).willReturn(Optional.of(anParticipant));
        given(mockParticipantRepository.existsById(1L)).willReturn(true);
        given(mockParticipantRepository.findAll()).willReturn(List.of(anParticipant));
        participantService.findById(1L);
        participantService.findAll(null, null);
        assertThat(cache.get(1L, ParticipantDTO.class)).isEqualTo(participantService.toDTO(anParticipant));
        assertThat(Objects.requireNonNull(cache.get("findAll_null_null", List.class)).size()).isEqualTo(1);

        participantService.delete(1L);

        assertThat(cache.get(1L)).isNull();
        assertThat(cache.get("findAll_null_null")).isNull();
    }

    @Test
    void givenCaching_whenCreate_thenFindAllCacheIsCleared(){
        var cache = Objects.requireNonNull(cacheManager.getCache("participantCache"));
        var anParticipant = new Participant(1L, "Participant1", true, LocalDate.of(1996, 1, 1), "Penguin");
        given(mockParticipantRepository.save(any(Participant.class))).willReturn(anParticipant);
        given(mockParticipantRepository.findAll()).willReturn(List.of(anParticipant));
        participantService.findAll(null, null);
        assertThat(Objects.requireNonNull(cache.get("findAll_null_null", List.class)).size()).isEqualTo(1);

        participantService.create(new ParticipantDTO(null, "Participant2", true, LocalDate.of(1996, 1, 1), "Penguin"));

        assertThat(cache.get("findAll_null_null")).isNull();
    }

}
