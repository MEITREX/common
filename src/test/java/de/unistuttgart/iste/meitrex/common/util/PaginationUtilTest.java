package de.unistuttgart.iste.meitrex.common.util;

import de.unistuttgart.iste.meitrex.generated.dto.Pagination;
import de.unistuttgart.iste.meitrex.generated.dto.PaginationInfo;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class PaginationUtilTest {

    /**
     * Given no pagination dto
     * When createPageable is called
     * Then an unpaged pageable is returned
     */
    @Test
    void testCreatePageableNoPaginationDto() {
        Pageable pageable = PaginationUtil.createPageable(null, null);

        assertThat(pageable.isPaged(), equalTo(false));

        pageable = PaginationUtil.createPageable(null, Sort.by("field"));

        assertThat(pageable.isPaged(), equalTo(false));
    }

    /**
     * Given a pagination dto
     * When createPageable is called
     * Then a pageable matching the pagination dto is returned
     */
    @Test
    void testCreatePageable() {
        final Pagination pagination = Pagination.builder()
                .setPage(1)
                .setSize(10)
                .build();

        final Pageable pageable = PaginationUtil.createPageable(pagination, Sort.unsorted());
        assertThat(pageable.isPaged(), equalTo(true));
        assertThat(pageable.getPageNumber(), equalTo(1));
        assertThat(pageable.getPageSize(), equalTo(10));

        assertThat(pageable.getSort(), equalTo(Sort.unsorted()));
    }

    /**
     * Given a number of total elements
     * When unpagedPaginationInfoDto is called
     * Then the correct pagination info dto is returned
     */
    @Test
    void testCreateUnpagedPaginationInfoDto() {
        final PaginationInfo paginationDto = PaginationUtil.unpagedPaginationInfo(10);

        assertThat(paginationDto.getPage(), equalTo(0));
        assertThat(paginationDto.getSize(), equalTo(10));
        assertThat(paginationDto.getTotalElements(), equalTo(10));
        assertThat(paginationDto.getTotalPages(), equalTo(1));
        assertThat(paginationDto.getHasNext(), equalTo(false));
    }

    /**
     * Given a page
     * When createPaginationInfoDto is called
     * Then the correct pagination info dto is returned
     */
    @Test
    void testCreatePaginationInfoDto() {
        final Page<?> page = new PageImpl<>(List.of(), PageRequest.of(0, 10), 20);
        final PaginationInfo paginationDto = PaginationUtil.createPaginationInfo(page);

        assertThat(paginationDto.getPage(), equalTo(0));
        assertThat(paginationDto.getSize(), equalTo(10));
        assertThat(paginationDto.getTotalElements(), equalTo(20));
        assertThat(paginationDto.getTotalPages(), equalTo(2));
        assertThat(paginationDto.getHasNext(), equalTo(true));
    }
}
