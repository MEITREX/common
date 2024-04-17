package de.unistuttgart.iste.meitrex.common.service;

import de.unistuttgart.iste.meitrex.common.exception.MeitrexNotFoundException;
import de.unistuttgart.iste.meitrex.common.persistence.MeitrexRepository;
import de.unistuttgart.iste.meitrex.common.service.AbstractCrudServiceTestImpl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

class AbstractCrudServiceTest {

    private AbstractCrudServiceTestImpl service;
    private MeitrexRepository<TestEntity, Long> repository;

    @BeforeEach
    public void setUp() {
        repository = Mockito.mock(TestRepository.class);
        service = new AbstractCrudServiceTestImpl(repository);
    }

    @Test
    void testGetAll() {
        when(repository.findAll()).thenReturn(List.of(new TestEntity(1L, "Test")));

        List<TestDto> result = service.getAll();

        assertThat(result, hasSize(1));
        assertThat(result.getFirst().getId(), is(1L));
        assertThat(result.getFirst().getName(), is("Test"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void testFind() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(new TestEntity(id, "Test")));

        Optional<TestDto> result = service.find(id);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getId(), is(1L));
        assertThat(result.get().getName(), is("Test"));

        verify(repository, times(1)).findById(id);
    }

    @Test
    void testFindNotFound() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<TestDto> result = service.find(id);

        assertThat(result.isEmpty(), is(true));
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testGetOrThrow() {
        Long id = 1L;
        when(repository.findByIdOrThrow(id)).thenReturn(new TestEntity(id, "Test"));

        TestDto result = service.getOrThrow(id);

        assertThat(result.getId(), is(1L));
        assertThat(result.getName(), is("Test"));

        verify(repository, times(1)).findByIdOrThrow(id);
    }

    @Test
    void testGetOrThrowNotFound() {
        Long id = 1L;
        when(repository.findByIdOrThrow(id)).thenThrow(new MeitrexNotFoundException("Entity not found."));

        assertThrows(MeitrexNotFoundException.class, () -> service.getOrThrow(id));

        verify(repository, times(1)).findByIdOrThrow(id);
    }

    @Test
    void testCreateEntity() {
        TestEntity entity = new TestEntity(1L, "Test");
        when(repository.save(any())).thenAnswer(returnsFirstArg());

        TestEntity result = service.createEntity(() -> entity);

        assertThat(result.getId(), is(1L));
        assertThat(result.getName(), is("Test"));

        verify(repository, times(1)).save(entity);
    }

    @Test
    void testCreateEntityFromInput() {
        when(repository.save(any())).thenAnswer(invocation -> {
            TestEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        TestEntity result = service.createEntity(new TestInputDto("Test"));

        assertThat(result.getId(), is(1L));
        assertThat(result.getName(), is("Test"));

        verify(repository, times(1)).save(any());
    }

    @Test
    void testCreate() {
        TestEntity entity = new TestEntity(1L, "Test");
        when(repository.save(any())).thenReturn(entity);

        TestDto result = service.create(() -> entity);

        assertThat(result.getId(), is(1L));
        assertThat(result.getName(), is("Test"));

        verify(repository, times(1)).save(entity);
    }

    @Test
    void testCreateFromInput() {
        when(repository.save(any())).thenAnswer(invocation -> {
            TestEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        TestDto result = service.create(new TestInputDto("Test"));

        assertThat(result.getId(), is(1L));
        assertThat(result.getName(), is("Test"));

        verify(repository, times(1)).save(any());
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        TestEntity entity = new TestEntity(id, "Test");

        when(repository.findByIdOrThrow(id)).thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);

        TestDto result = service.update(id, e -> e.setName("Updated"));

        assertThat(result.getId(), is(1L));
        assertThat(result.getName(), is("Updated"));

        verify(repository, times(1)).findByIdOrThrow(id);
        verify(repository, times(1)).save(any());
    }

    @Test
    void testUpdateNotFound() {
        Long id = 1L;
        when(repository.findByIdOrThrow(id)).thenThrow(new MeitrexNotFoundException("Entity not found."));

        assertThrows(MeitrexNotFoundException.class, () -> service.update(id, e -> e.setName("Updated")));

        verify(repository, times(1)).findByIdOrThrow(id);
        verify(repository, never()).save(any());
    }

    @Test
    void testUpdateFromInput() {
        Long id = 1L;
        TestEntity entity = new TestEntity(id, "Test");

        when(repository.findByIdOrThrow(id)).thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);

        TestDto result = service.update(id, new TestInputDto("Updated"));

        assertThat(result.getId(), is(1L));
        assertThat(result.getName(), is("Updated"));

        verify(repository, times(1)).findByIdOrThrow(id);
        verify(repository, times(1)).save(any());
    }

    @Test
    void testDelete() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        boolean result = service.delete(id);

        assertThat(result, is(true));

        verify(repository, times(1)).existsById(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteNotFound() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(false);

        boolean result = service.delete(id);

        assertThat(result, is(false));

        verify(repository, times(1)).existsById(id);
        verify(repository, never()).deleteById(id);
    }
}