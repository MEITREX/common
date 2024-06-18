package de.unistuttgart.iste.meitrex.common.service;

import de.unistuttgart.iste.meitrex.common.exception.MeitrexNotFoundException;
import de.unistuttgart.iste.meitrex.common.persistence.IWithId;
import de.unistuttgart.iste.meitrex.common.persistence.MeitrexRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Abstract service for CRUD operations.
 * Services extending this class can utilize the helper methods to perform CRUD operations on entities.
 *
 * @param <I> the type of the entity id, e.g., UUID
 * @param <E> the type of the entity, e.g., UserEntity
 * @param <D> the type of the DTO, e.g., User
 */
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractCrudService<I, E extends IWithId<I>, D> {

    private final MeitrexRepository<E, I> repository;
    private final ModelMapper modelMapper;
    private final Class<E> entityClass;
    private final Class<D> dtoClass;

    /**
     * @param repository  used to access the entities
     * @param modelMapper used to map between entities and DTOs. The model mapper should be set up to map
     *                    between the entity and DTO classes,and between input classes and the entity class.
     * @param entityClass the class of the entity, e.g., UserEntity.class
     * @param dtoClass    the class of the DTO, e.g., User.class
     */
    protected AbstractCrudService(MeitrexRepository<E, I> repository,
                                  ModelMapper modelMapper,
                                  Class<E> entityClass,
                                  Class<D> dtoClass) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    /**
     * Get all entities from the repository and convert them to DTOs.
     *
     * @return a list of all entities as DTOs
     */
    protected List<D> getAll() {
        final List<E> entities = getRepository().findAll();
        return convertToDtos(entities);
    }

    /**
     * Find an entity by its id and convert it to a DTO.
     *
     * @param id the id of the entity to find
     * @return the entity as a DTO, or an empty optional if no entity with the given id exists
     */
    protected Optional<D> find(final I id) {
        return getRepository()
                .findById(id)
                .map(this::convertToDto);
    }

    /**
     * Gets an entity by its id and convert it to a DTO.
     * If no entity with the given id exists, throws an exception.
     *
     * @param id the id of the entity to get
     * @return the entity as a DTO
     * @throws MeitrexNotFoundException if no entity with the given id exists
     */
    protected D getOrThrow(final I id) {
        return convertToDto(getRepository().findByIdOrThrow(id));
    }

    /**
     * Creates an entity using the given entity creator and saves it to the repository.
     *
     * @param entityCreator supplier that returns the entity to create
     * @return the created entity as an entity
     */
    protected E createEntity(final Supplier<E> entityCreator) {
        final E entity = entityCreator.get();
        return getRepository().save(entity);
    }

    /**
     * Creates an entity by mapping the given input to an entity and saves it to the repository.
     * Note: This requires the model mapper to be set up correctly.
     *
     * @param createInput the input to map to an entity
     * @return the created entity as an entity
     */
    protected E createEntity(final Object createInput) {
        return createEntity(() -> getModelMapper().map(createInput, getEntityClass()));
    }

    /**
     * Creates an entity using the given entity creator, saves it to the repository
     * and converts it to a DTO.
     *
     * @param entityCreator supplier that returns the entity to create
     * @return the created entity as a DTO
     */
    protected D create(final Supplier<E> entityCreator) {
        final E entity = createEntity(entityCreator);

        return convertToDto(entity);
    }

    /**
     * Creates an entity by mapping the given input to an entity, saves it to the repository
     * and converts it to a DTO.
     * Note: This requires the model mapper to be set up correctly.
     *
     * @param createInput the input to map to an entity
     * @return the created entity as a DTO
     */
    protected D create(final Object createInput) {
        return create(() -> getModelMapper().map(createInput, getEntityClass()));
    }

    /**
     * Updates an entity by its id using the given entity updater and saves it to the repository.
     *
     * @param id            the id of the entity to update
     * @param entityUpdater consumer that updates the entity
     * @return the updated entity as a DTO
     */
    protected D update(final I id, final Consumer<E> entityUpdater) {
        final E entity = getRepository().findByIdOrThrow(id);
        entityUpdater.accept(entity);

        final E savedEntity = getRepository().save(entity);

        return convertToDto(savedEntity);
    }

    /**
     * Updates an entity by its id by mapping the given input
     * to the existing entity and saves it to the repository.
     * This will not overwrite the entity, but update only the fields
     * that are defined in the input.
     *
     * @param id          the id of the entity to update
     * @param updateInput the input to map to an entity
     * @return the updated entity as a DTO
     */
    protected D update(final I id, final Object updateInput) {
        return update(id, entity -> getModelMapper().map(updateInput, entity));
    }

    /**
     * Deletes an entity by its id.
     *
     * @param id the id of the entity to delete
     * @return true if the entity was deleted, false if no entity with the given id exists
     * @apiNote subclasses will likely require a more sophisticated deletion logic
     */
    protected boolean delete(final I id) {
        if (!getRepository().existsById(id)) {
            return false;
        }

        getRepository().deleteById(id);
        return true;
    }

    /**
     * Converts an entity to a DTO.
     *
     * @param entity the entity to convert
     * @return the entity as a DTO or null if the entity is null
     */
    protected D convertToDto(final E entity) {
        if (entity == null) {
            return null;
        }
        return getModelMapper().map(entity, getDtoClass());
    }

    /**
     * Converts a list of entities to a list of DTOs.
     *
     * @param entities the entities to convert
     * @return the entities as DTOs
     */
    protected List<D> convertToDtos(final List<E> entities) {
        return entities.stream()
                .map(this::convertToDto)
                .toList();
    }
}
