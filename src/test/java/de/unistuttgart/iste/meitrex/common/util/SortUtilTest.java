package de.unistuttgart.iste.meitrex.common.util;

import de.unistuttgart.iste.meitrex.generated.dto.SortDirection;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link SortUtil}.
 */
class SortUtilTest {

    /**
     * Given no sort fields
     * When createSort is called
     * Then an unsorted sort object is returned
     */
    @Test
    void testUnsortedWhenSortFieldsEmpty() {
        assertThat(SortUtil.createSort(null, null), equalTo(Sort.unsorted()));
        assertThat(SortUtil.createSort(List.of(), null), equalTo(Sort.unsorted()));
    }

    /**
     * Given a single sort field
     * When createSort is called
     * Then a sort object with the given sort field is returned
     */
    @Test
    void testSingleSortField() {
        Sort sort = SortUtil.createSort(List.of("field"), List.of(SortDirection.ASC));

        assertThat(sort.isSorted(), equalTo(true));
        // count() is used to check that there is only one sort field
        assertThat(sort.stream().count(), is(1L));
        Sort.Order order = requireNonNull(sort.getOrderFor("field"));
        assertThat(order.getProperty(), equalTo("field"));
        assertThat(order.getDirection(), equalTo(Sort.Direction.ASC));

        sort = SortUtil.createSort(List.of("field"), List.of(SortDirection.DESC));
        assertThat(sort.isSorted(), equalTo(true));
        order = requireNonNull(sort.getOrderFor("field"));
        assertThat(order.getProperty(), equalTo("field"));
        assertThat(order.getDirection(), equalTo(Sort.Direction.DESC));
    }

    /**
     * Given no sort direction
     * When createSort is called
     * Then a sort object with the given sort field is returned, sorted ascending
     */
    @Test
    void testDefaultDirectionIsAsc() {
        Sort sort = SortUtil.createSort(List.of("field"), null);

        Sort.Order order = requireNonNull(sort.getOrderFor("field"));
        assertThat(order.getDirection(), equalTo(Sort.Direction.ASC));

        sort = SortUtil.createSort(List.of("field"), List.of());
        order = requireNonNull(sort.getOrderFor("field"));
        assertThat(order.getDirection(), equalTo(Sort.Direction.ASC));
    }

    /**
     * Given multiple sort fields
     * When createSort is called
     * Then a sort object with the given sort fields is returned
     */
    @Test
    void testMultipleSortFields() {
        final Sort sort = SortUtil.createSort(List.of("field1", "field2"),
                List.of(SortDirection.ASC, SortDirection.DESC));

        assertThat(sort.isSorted(), equalTo(true));
        // count() is used to check that there are exactly two sort fields
        assertThat(sort.stream().count(), is(2L));
        Sort.Order order = requireNonNull(sort.getOrderFor("field1"));
        assertThat(order.getProperty(), equalTo("field1"));
        assertThat(order.getDirection(), equalTo(Sort.Direction.ASC));

        order = requireNonNull(sort.getOrderFor("field2"));
        assertThat(order.getProperty(), equalTo("field2"));
        assertThat(order.getDirection(), equalTo(Sort.Direction.DESC));
    }
}
