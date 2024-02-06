package de.unistuttgart.iste.meitrex.common.util;

import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MeitrexCollectionUtils {

    /**
     * Counts the number of elements in the collection that match the given filter.
     *
     * @param collection the collection to count the elements in
     * @param filter     the filter to apply
     * @param <T>        the type of the elements in the collection
     * @return the number of elements in the collection that match the given filter
     */
    public static <T> long count(final Collection<? extends T> collection, final Predicate<? super T> filter) {
        return collection.stream().filter(filter).count();
    }

    /**
     * Like {@link #count(Collection, Predicate)} but returns an int instead of a long.
     */
    public static <T> int countAsInt(final Collection<? extends T> collection, final Predicate<? super T> filter) {
        return (int) count(collection, filter);
    }

    /**
     * Groups a collection of elements into a list of sub-lists,
     * where each sub-list contains all elements that have the same key.
     * The sub-lists are sorted by the given list of keys.
     *
     * @param elements         the elements to group
     * @param sortedKeys       the correct order of the keys and the corresponding sub-lists
     * @param groupingFunction the function to get the key from an element
     * @param <K>              type of the keys
     * @param <T>              type of the elements
     * @return list of sub-lists
     */
    public static <K, T> List<List<T>> groupIntoSubLists(final Collection<T> elements,
                                                         final List<K> sortedKeys,
                                                         final Function<T, K> groupingFunction) {
        final Map<K, List<T>> map = elements.stream()
                .collect(Collectors.groupingBy(groupingFunction));
        return mapToSortedList(map, sortedKeys, List.of());
    }

    /**
     * Sorts a collection of elements by a given list of keys.
     * The elements are sorted by the order of the keys.
     * If any key is not found in the elements, null is used as the default value.
     *
     * @param elements         list of elements
     * @param sortedKeys       list of keys
     * @param groupingFunction function to get the key from an element
     * @param <K>              type of the keys
     * @param <T>              type of the elements
     * @return list of elements, sorted by the keys, potentially with null values.
     * This list has the same size as the list of keys.
     */
    public static <K, T> List<T> sortByKeys(final Collection<T> elements,
                                            final List<K> sortedKeys,
                                            final Function<T, K> groupingFunction) {
        final Map<K, T> map = elements.stream()
                .collect(Collectors.toMap(groupingFunction, Function.identity()));
        return mapToSortedList(map, sortedKeys, null);
    }

    /**
     * Converts a map to a list of entries, sorted by a given list of keys.
     *
     * @param keyToObjectMap map of keys to objects of type T
     * @param keysSorted     list of UUIDs
     * @param defaultValue   default value if the key is not found in the map
     * @param <T>            type of the objects
     * @param <K>            type of the keys
     * @return list of objects of type T
     */
    private static <K, T> List<T> mapToSortedList(final Map<K, T> keyToObjectMap,
                                                  final List<K> keysSorted,
                                                  final T defaultValue) {
        return keysSorted.stream()
                .map(key -> keyToObjectMap.getOrDefault(key, defaultValue))
                .toList();
    }
}
