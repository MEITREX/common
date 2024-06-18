package de.unistuttgart.iste.meitrex.common.persistence;

import de.unistuttgart.iste.meitrex.common.exception.MeitrexNotFoundException;
import de.unistuttgart.iste.meitrex.common.exception.ResourceAlreadyExistsException;
import de.unistuttgart.iste.meitrex.common.util.MeitrexCollectionUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository that provides additional methods for querying entities and preserving the order.
 *
 * @param <T>  The type of the entity.
 * @param <ID> The type of the id of the entity.
 */
@SuppressWarnings("java:S119") // "ID" is a meaningful name for the generic type
public interface MeitrexRepository<T extends IWithId<ID>, ID> extends JpaRepository<T, ID> {

    /**
     * Finds all entities with the given ids and preserves the order of the ids.
     * If an entity is not found, null is returned at the corresponding position.
     *
     * @param ids The ids of the entities to find.
     * @return The entities with the given ids in order of the given ids.
     */
    default List<T> findAllByIdPreservingOrder(final List<ID> ids) {
        final List<T> allById = findAllById(ids);

        return MeitrexCollectionUtils.sortByKeys(allById, ids, IWithId::getId);
    }

    /**
     * Gets all entities with the given ids and preserves the order of the ids.
     * If an entity is not found, an MeitrexNotFoundException is thrown.
     *
     * @param ids The ids of the entities to get.
     * @return The entities with the given ids in order of the given ids.
     */
    default List<T> getAllByIdPreservingOrder(final List<ID> ids) {
        final List<T> allById = findAllByIdPreservingOrder(ids);

        final List<ID> missingIds = new ArrayList<>();

        // check for null values, meaning that the entity was not found
        for (int i = 0; i < ids.size(); i++) {
            if (allById.get(i) == null) {
                missingIds.add(ids.get(i));
            }
        }

        if (!missingIds.isEmpty()) {
            String missingIdsString = missingIds.stream().map(ID::toString).collect(Collectors.joining(", "));

            throw new MeitrexNotFoundException("Entities(s) with id(s) " + missingIdsString + " not found");
        }

        return allById;
    }

    /**
     * Finds an entity by its id or throws a ResourceNotFoundException if the entity is not found.
     * This method is similar to {@link JpaRepository#getReferenceById(Object)},
     * but throws a custom MeitrexNotFoundException instead of an EntityNotFoundException.
     *
     * @param id The id of the entity to find.
     * @return The entity with the given id.
     * @throws MeitrexNotFoundException If the entity is not found.
     */
    default T findByIdOrThrow(final ID id) {
        return findById(id).orElseThrow(() -> new MeitrexNotFoundException(getEntityName(), id));
    }

    /**
     * Same as {@link #findByIdOrThrow(Object)}, but depending on the context, the naming
     * of this method might be more appropriate.
     *
     * @see #findByIdOrThrow(Object)
     */
    default T requireExists(final ID id) {
        return findByIdOrThrow(id);
    }

    /**
     * Checks if an entity with the given id exists and throws an exception if it does.
     *
     * @param id The id of the entity to check.
     * @throws ResourceAlreadyExistsException If the entity exists.
     */
    default void requireNotExists(final ID id) {
        if (existsById(id)) {
            throw new ResourceAlreadyExistsException(getEntityName(), id);
        }
    }

    /**
     * Gets the name of the entity for error messages.
     * Sub-classes should override this method to provide a name.
     * By default, the name is derived from the repository class name.
     *
     * @return The name of the entity.
     */
    default String getEntityName() {
        return getClass().getSimpleName().replace("Repository", "");
    }
}
