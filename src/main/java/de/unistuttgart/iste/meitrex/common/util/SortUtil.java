package de.unistuttgart.iste.meitrex.common.util;

import de.unistuttgart.iste.meitrex.generated.dto.SortDirection;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Utility class for sorting.
 */
public class SortUtil {

    private SortUtil() {
        // Utility class
    }

    /**
     * Creates a {@link Sort} object from the given sort fields and sort directions.
     *
     * @param sortFields     the sort fields, may be empty or null.
     * @param sortDirections the sort directions, may be empty or null. If it has fewer elements than sortFields,
     *                       the remaining sort fields will be sorted ascending.
     * @return the created {@link Sort} object. If sortField is empty or null, {@link Sort#unsorted()} is returned.
     */
    @NonNull
    public static Sort createSort(@Nullable final List<String> sortFields, @Nullable final List<SortDirection> sortDirections) {
        Sort sort = Sort.unsorted();
        if (sortFields == null) {
            return sort;
        }

        for (int index = 0; index < sortFields.size(); index++) {
            final Sort.Direction sortDirection = getSortDirection(sortDirections, index);

            sort = sort.and(Sort.by(sortDirection, sortFields.get(index)));
        }

        return sort;
    }

    @NonNull
    private static Sort.Direction getSortDirection(final List<SortDirection> sortDirections, final int index) {
        if (sortDirections == null || index >= sortDirections.size()) {
            return Sort.Direction.ASC;
        }
        return sortDirections.get(index) == SortDirection.ASC
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
    }
}
