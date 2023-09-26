package de.unistuttgart.iste.gits.common.persistence;

/**
 * Interface for objects that have an id.
 *
 * @param <I> The type of the id, e.g. UUID.
 */
public interface IWithId<I> {

    /**
     * Gets the id of the object.
     *
     * @return The id of the object.
     */
    I getId();

}
