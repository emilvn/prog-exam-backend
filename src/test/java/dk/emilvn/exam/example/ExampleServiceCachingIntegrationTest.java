package dk.emilvn.exam.example;

import dk.emilvn.exam.config.CacheConfig;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CacheConfig.class, ExampleService.class})
@EnableCaching
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class,
        RedisAutoConfiguration.class
})
class ExampleServiceCachingIntegrationTest {

    @MockBean
    private ExampleRepository mockExampleRepository;

    @Autowired
    private ExampleService exampleService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void clearCache() {
        Objects.requireNonNull(cacheManager.getCache("exampleCache")).clear();
    }

    @Test
    void givenCaching_whenFindExampleById_thenExampleReturnedFromCache() {
        var cache = Objects.requireNonNull(cacheManager.getCache("exampleCache"));
        var anExample = new Example(1L, "Example1");
        given(mockExampleRepository.findById(1L)).willReturn(Optional.of(anExample));

        var exampleCacheMiss = exampleService.findById(1L);
        var exampleCacheHit = exampleService.findById(1L);

        assertThat(exampleCacheMiss).isEqualTo(exampleService.toDTO(anExample));
        assertThat(exampleCacheHit).isEqualTo(exampleService.toDTO(anExample));

        verify(mockExampleRepository, times(1)).findById(1L);
        assertThat(cache.get(1L, ExampleDTO.class)).isEqualTo(exampleService.toDTO(anExample));
    }

    @Test
    void givenCaching_whenFindAll_thenExamplesReturnedFromCache() {
        var cache = Objects.requireNonNull(cacheManager.getCache("exampleCache"));
        var anExample = new Example(1L, "Example1");
        var anotherExample = new Example(2L, "Example2");
        var examples = List.of(anExample, anotherExample);
        given(mockExampleRepository.findAll()).willReturn(examples);

        var examplesCacheMiss = exampleService.findAll(null, null);
        var examplesCacheHit = exampleService.findAll(null, null);

        assertThat(examplesCacheMiss).isEqualTo(List.of(exampleService.toDTO(anExample), exampleService.toDTO(anotherExample)));
        assertThat(examplesCacheHit).isEqualTo(List.of(exampleService.toDTO(anExample), exampleService.toDTO(anotherExample)));

        verify(mockExampleRepository, times(1)).findAll();
        assertThat(Objects.requireNonNull(cache.get("findAll_null_null", List.class)).size()).isEqualTo(2);
    }

    @Test
    void givenCaching_whenCreate_thenExampleCached() {
        var cache = Objects.requireNonNull(cacheManager.getCache("exampleCache"));
        var anExample = new Example(1L, "Example1");
        given(mockExampleRepository.save(any(Example.class))).willReturn(anExample);
        var exampleDTO = new ExampleDTO(null, "Example1");

        var createdExample = exampleService.create(exampleDTO);

        assertThat(createdExample).isEqualTo(exampleService.toDTO(anExample));
        assertThat(cache.get(createdExample.id(), ExampleDTO.class)).isEqualTo(createdExample);
    }

    @Test
    void givenCaching_whenUpdate_thenExampleUpdatedInCache() {
        var cache = Objects.requireNonNull(cacheManager.getCache("exampleCache"));
        var anExample = new Example(1L, "Example1");
        given(mockExampleRepository.findById(1L)).willReturn(Optional.of(anExample));

        var updatedExample = new ExampleDTO(1L, "UpdatedName");
        var updatedResult = exampleService.update(1L, updatedExample);

        assertThat(updatedResult).isEqualTo(updatedExample);
        assertThat(cache.get(1L, ExampleDTO.class)).isEqualTo(updatedExample);
    }

    @Test
    void givenCaching_whenPatch_thenExamplePatchedInCache() {
        var cache = Objects.requireNonNull(cacheManager.getCache("exampleCache"));
        var anExample = new Example(1L, "Example1");
        given(mockExampleRepository.findById(1L)).willReturn(Optional.of(anExample));

        var patchedExample = new ExampleDTO(1L, "PatchedName");
        var patchedResult = exampleService.patch(1L, patchedExample);

        assertThat(patchedResult).isEqualTo(patchedExample);
        assertThat(cache.get(1L, ExampleDTO.class)).isEqualTo(patchedExample);
    }

    @Test
    void givenCaching_whenDelete_thenExampleRemovedFromCache() {
        var cache = Objects.requireNonNull(cacheManager.getCache("exampleCache"));
        given(mockExampleRepository.existsById(1L)).willReturn(true);

        exampleService.delete(1L);

        verify(mockExampleRepository, times(1)).deleteById(1L);
        assertThat(cache.get(1L)).isNull();
    }

    @Test
    void givenCaching_whenDelete_thenCacheIsCleared(){
        var cache = Objects.requireNonNull(cacheManager.getCache("exampleCache"));
        var anExample = new Example(1L, "Example1");
        given(mockExampleRepository.findById(1L)).willReturn(Optional.of(anExample));
        given(mockExampleRepository.existsById(1L)).willReturn(true);
        given(mockExampleRepository.findAll()).willReturn(List.of(anExample));
        exampleService.findById(1L);
        exampleService.findAll(null, null);
        assertThat(cache.get(1L, ExampleDTO.class)).isEqualTo(exampleService.toDTO(anExample));
        assertThat(Objects.requireNonNull(cache.get("findAll_null_null", List.class)).size()).isEqualTo(1);

        exampleService.delete(1L);

        assertThat(cache.get(1L)).isNull();
        assertThat(cache.get("findAll_null_null")).isNull();
    }

    @Test
    void givenCaching_whenCreate_thenFindAllCacheIsCleared(){
        var cache = Objects.requireNonNull(cacheManager.getCache("exampleCache"));
        var anExample = new Example(1L, "Example1");
        given(mockExampleRepository.save(any(Example.class))).willReturn(anExample);
        given(mockExampleRepository.findAll()).willReturn(List.of(anExample));
        exampleService.findAll(null, null);
        assertThat(Objects.requireNonNull(cache.get("findAll_null_null", List.class)).size()).isEqualTo(1);

        exampleService.create(new ExampleDTO(null, "Example2"));

        assertThat(cache.get("findAll_null_null")).isNull();
    }

}
