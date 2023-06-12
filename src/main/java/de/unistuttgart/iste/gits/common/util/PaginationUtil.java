package de.unistuttgart.iste.gits.common.util;

import de.unistuttgart.iste.gits.generated.dto.Pagination;
import de.unistuttgart.iste.gits.generated.dto.PaginationInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

/**
 * Utility class for mapping {@link Pagination}s to {@link Pageable}s
 * and {@link Page}s to {@link PaginationInfo}s.
 */
public class PaginationUtil {

    private PaginationUtil() {
        // Utility class
    }

    /**
     * Creates a {@link Pageable} matching the given {@link Pagination} and {@link Sort}.
     *
     * @param pagination the {@link Pagination} to create the {@link Pageable} from.
     * @param sort       the {@link Sort} to use for the {@link Pageable}.
     * @return the created {@link Pageable}.
     * If the given {@link Pagination} is null, {@link Pageable#unpaged()} is returned.
     */
    public static Pageable createPageable(@Nullable Pagination pagination, Sort sort) {
        if (pagination == null) {
            return Pageable.unpaged();
        }
        return PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                sort
        );
    }

    /**
     * Creates a {@link PaginationInfo} from the given {@link Page}.
     *
     * @param result the {@link Page} to create the {@link PaginationInfo} from.
     * @return the created {@link PaginationInfo}.
     */
    public static PaginationInfo createPaginationInfo(Page<?> result) {
        return PaginationInfo.builder()
                .setPage(result.getNumber())
                .setSize(result.getSize())
                .setTotalElements((int) result.getTotalElements())
                .setTotalPages(result.getTotalPages())
                .setHasNext(result.hasNext())
                .build();
    }

    /**
     * Creates a {@link PaginationInfo} for an unpaged query.
     *
     * @param totalElements the total number of elements in the query.
     * @return the created {@link PaginationInfo},
     * which has page 0, size totalElements, totalPages 1 and hasNext false.
     */
    public static PaginationInfo unpagedPaginationInfo(int totalElements) {
        return PaginationInfo.builder()
                .setPage(0)
                .setSize(totalElements)
                .setTotalElements(totalElements)
                .setTotalPages(1)
                .setHasNext(false)
                .build();
    }
}
