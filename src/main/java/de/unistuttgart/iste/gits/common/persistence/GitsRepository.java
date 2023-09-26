package de.unistuttgart.iste.gits.common.persistence;

import de.unistuttgart.iste.gits.common.util.GitsCollectionUtils;
import jakarta.persistence.EntityNotFoundException;
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
public interface GitsRepository<T extends IWithId<ID>, ID> extends JpaRepository<T, ID> {

    /**
     * Finds all entities with the given ids and preserves the order of the ids.
     * If an entity is not found, null is returned at the corresponding position.
     *
     * @param ids The ids of the entities to find.
     * @return The entities with the given ids in order of the given ids.
     */
    default List<T> findAllByIdPreservingOrder(final List<ID> ids) {
        final List<T> allById = findAllById(ids);

        return GitsCollectionUtils.sortByKeys(allById, ids, IWithId::getId);
    }

    /**
     * Gets all entities with the given ids and preserves the order of the ids.
     * If an entity is not found, an EntityNotFoundException is thrown.
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
            throw new EntityNotFoundException("Entities(s) with id(s) "
                                              + missingIds.stream().map(ID::toString)
                                                      .collect(Collectors.joining(", "))
                                              + " not found");
        }

        return allById;
    }
}
