package de.unistuttgart.iste.gits.common.util;

import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.function.Predicate;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class StreamUtils {

    /**
     * Counts the number of elements in the collection that match the given filter.
     *
     * @param collection the collection to count the elements in
     * @param filter     the filter to apply
     * @param <T>        the type of the elements in the collection
     * @return the number of elements in the collection that match the given filter
     */
    public static <T> long count(Collection<? extends T> collection, Predicate<? super T> filter) {
        return collection.stream().filter(filter).count();
    }
}
