package de.unistuttgart.iste.gits.common.util;

import de.unistuttgart.iste.gits.generated.dto.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Utility class for creating specifications for JPA queries.
 * <p>
 * Contains methods for creating specifications for filtering by string, boolean, integer and date time values
 * using the filter DTOs.
 *
 * @implNote The sonar rule S119 is disabled for this class, as the default naming convention for generic types
 * (only a single letter allowed) is less readable than the one used here.
 */
@SuppressWarnings("java:S119")
public class SpecificationUtil {

    private SpecificationUtil() {
        // utility class
    }

    private static final Specification<?> ALWAYS_TRUE = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

    private static final Specification<?> ALWAYS_FALSE = (root, query, criteriaBuilder) -> criteriaBuilder.disjunction();

    /**
     * Creates a specification for the given string filter.
     *
     * @param field        the field to filter by
     * @param stringFilter the string filter DTO, may be null
     * @param <T>          generic type to allow this filter to be used with any entity type
     * @return the specification for the given string filter,
     * or {@link SpecificationUtil#alwaysTrue()} if the filter is null
     */
    public static <T> Specification<T> stringFilter(final String field, @Nullable final StringFilter stringFilter) {
        if (stringFilter == null) {
            return alwaysTrue();
        }

        return Specification
                .<T>where(stringEqualTo(field, stringFilter.getEquals(), stringFilter.getIgnoreCase()))
                .and(contains(field, stringFilter.getContains(), stringFilter.getIgnoreCase()));
    }

    /**
     * Creates a specification for the given date time filter.
     *
     * @param field          the field to filter by
     * @param dateTimeFilter the date time filter DTO, may be null
     * @param <T>            generic type to allow this filter to be used with any entity type
     * @return the specification for the given date time filter,
     * or {@link SpecificationUtil#alwaysTrue()} if the filter is null
     */
    public static <T> Specification<T> dateTimeFilter(final String field, @Nullable final DateTimeFilter dateTimeFilter) {
        if (dateTimeFilter == null) {
            return alwaysTrue();
        }

        return Specification.<T>where(lessThan(field, dateTimeFilter.getBefore()))
                .and(greaterThan(field, dateTimeFilter.getAfter()));
    }

    /**
     * Creates a specification for the given integer filter.
     *
     * @param field     the field to filter by
     * @param intFilter the integer filter DTO, may be null
     * @param <T>       generic type to allow this filter to be used with any entity type
     * @return the specification for the given integer filter,
     * or {@link SpecificationUtil#alwaysTrue()} if the filter is null
     */
    public static <T> Specification<T> intFilter(final String field, @Nullable final IntFilter intFilter) {
        if (intFilter == null) {
            return alwaysTrue();
        }

        return Specification.<T>where(equalTo(field, intFilter.getEquals()))
                .and(lessThan(field, intFilter.getLessThan()))
                .and(greaterThan(field, intFilter.getGreaterThan()));
    }

    /**
     * Creates a specification for the "not" filter.
     * This filter is used to negate another filter.
     *
     * @param not                the filter to negate, may be null.
     * @param dtoToSpecification the function to convert the filter DTO to a specification.
     * @param <EntityType>       the type of the entity.
     * @param <DtoType>          the type of the filter DTO.
     * @return the specification for the "not" filter, or {@link SpecificationUtil#alwaysTrue()} if the filter is null.
     */
    public static <EntityType, DtoType> Specification<EntityType> not(
            final DtoType not,
            final Function<DtoType, Specification<EntityType>> dtoToSpecification) {

        return Optional.ofNullable(not)
                .map(dtoToSpecification)
                .map(Specification::not)
                .orElse(alwaysTrue());
    }

    /**
     * Creates a specification for combining multiple filter DTOs with "and".
     *
     * @param ands               the filter DTOs to combine, may be null.
     * @param dtoToSpecification the function to convert the filter DTO to a specification.
     * @param <EntityType>       the type of the entity.
     * @param <DtoType>          the type of the filter DTO.
     * @return the specification for the "and" filter, or {@link SpecificationUtil#alwaysTrue()} if the filter is null.
     */
    public static <EntityType, DtoType> Specification<EntityType> and(
            @Nullable final List<DtoType> ands,
            final Function<DtoType, Specification<EntityType>> dtoToSpecification) {

        return Stream.ofNullable(ands)
                .flatMap(List::stream)
                .map(dtoToSpecification)
                .reduce(alwaysTrue(), Specification::and);
    }

    /**
     * Creates a specification for combining multiple filter DTOs with "or".
     *
     * @param ors                the filter DTOs to combine, may be null.
     * @param dtoToSpecification the function to convert the filter DTO to a specification.
     * @param <EntityType>       the type of the entity.
     * @param <DtoType>          the type of the filter DTO.
     * @return the specification for the "or" filter, or {@link SpecificationUtil#alwaysFalse()} if the filter is null.
     */
    public static <EntityType, DtoType> Specification<EntityType> or(
            @Nullable final List<DtoType> ors,
            final Function<DtoType, Specification<EntityType>> dtoToSpecification) {

        return Stream.ofNullable(ors)
                .flatMap(List::stream)
                .map(dtoToSpecification)
                .reduce(Specification::or)
                // otherwise the result must be always false because (something OR true) would be always true
                .orElse(alwaysFalse());
    }

    /**
     * Creates a specification for filtering by a boolean value.
     *
     * @param field the field to filter by
     * @param value the value to filter by, may be null
     * @param <T>   generic type to allow this filter to be used with any entity type
     * @return the specification that checks if the field equals the value,
     * or {@link SpecificationUtil#alwaysTrue()} if the value is null
     */
    public static <T> Specification<T> booleanFilter(final String field, @Nullable final Boolean value) {
        return equalTo(field, value);
    }

    /**
     * Creates a specification that checks if the given field equals the given value.
     *
     * @param field the field to filter by
     * @param value the value the field should be equal to, may be null
     * @param <T>   generic type to allow this filter to be used with any entity type
     * @return the specification that checks if the field equals the value,
     * or {@link SpecificationUtil#alwaysTrue()} if the value is null
     */
    public static <T> Specification<T> equalTo(final String field, @Nullable final Object value) {
        if (value == null) {
            return alwaysTrue();
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value);
    }

    /**
     * Creates a specification that checks if the given string field equals the given value.
     *
     * @param field      the field to filter by
     * @param value      the value the field should be equal to, may be null
     * @param ignoreCase whether to ignore case when comparing the field and the value
     * @param <T>        generic type to allow this filter to be used with any entity type
     * @return the specification that checks if the field equals the value,
     * or {@link SpecificationUtil#alwaysTrue()} if the value is null
     */
    public static <T> Specification<T> stringEqualTo(final String field, @Nullable final String value, final boolean ignoreCase) {
        if (value == null) {
            return alwaysTrue();
        }
        return (root, query, criteriaBuilder) -> {
            final Expression<String> path = toLowerCasePathIfIgnoreCase(field, root, criteriaBuilder, ignoreCase);
            return criteriaBuilder.equal(path, toLowerCaseIfIgnoreCase(value, ignoreCase));
        };
    }

    /**
     * Creates a specification that checks if the given string value is contained in the given field.
     *
     * @param field      the field to filter by
     * @param value      the value the field should contain, may be null
     * @param ignoreCase whether to ignore case when comparing the field and the value
     * @param <T>        generic type to allow this filter to be used with any entity type
     * @return the specification that checks if the field contains the value,
     * or {@link SpecificationUtil#alwaysTrue()} if the value is null
     */
    public static <T> Specification<T> contains(final String field, final String value, final boolean ignoreCase) {
        if (value == null) {
            return alwaysTrue();
        }
        return (root, query, criteriaBuilder) -> {
            final Expression<String> path = toLowerCasePathIfIgnoreCase(field, root, criteriaBuilder, ignoreCase);

            // use the sql wildcard character % to allow any characters before and after the value
            return criteriaBuilder.like(path, "%" + toLowerCaseIfIgnoreCase(value, ignoreCase) + "%");
        };
    }

    /**
     * Creates a specification that checks if the given value is less than the given field.
     *
     * @param field the field to filter by
     * @param value the value the field should be less than, may be null
     * @param <T>   generic type to allow this filter to be used with any entity type
     * @param <C>   type of the value to compare, must be comparable
     * @return the specification that checks if the field is less than the value,
     * or {@link SpecificationUtil#alwaysTrue()} if the value is null
     */
    public static <T, C extends Comparable<C>> Specification<T> lessThan(final String field, @Nullable final C value) {
        if (value == null) {
            return alwaysTrue();
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(field), value);
    }

    /**
     * Creates a specification that checks if the given value is greater than the given field.
     *
     * @param field the field to filter by
     * @param value the value the field should be greater than, may be null
     * @param <T>   generic type to allow this filter to be used with any entity type
     * @param <C>   type of the value to compare, must be comparable
     * @return the specification that checks if the field is greater than the value,
     * or {@link SpecificationUtil#alwaysTrue()} if the value is null
     */
    public static <T, C extends Comparable<C>> Specification<T> greaterThan(final String field, @Nullable final C value) {
        if (value == null) {
            return alwaysTrue();
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(field), value);
    }

    /**
     * Specification for a filter that does not filter out anything, i.e. always true.
     */
    @SuppressWarnings("unchecked")
    public static <T> Specification<T> alwaysTrue() {
        return (Specification<T>) ALWAYS_TRUE;
    }

    /**
     * Specification for a filter that filters out everything, i.e. always false.
     */
    @SuppressWarnings("unchecked")
    public static <T> Specification<T> alwaysFalse() {
        return (Specification<T>) ALWAYS_FALSE;
    }

    private static Expression<String> toLowerCasePathIfIgnoreCase(final String field,
                                                                  final Root<?> root,
                                                                  final CriteriaBuilder criteriaBuilder,
                                                                  final boolean ignoreCase) {
        return ignoreCase ? criteriaBuilder.lower(root.get(field)) : root.get(field);
    }

    private static String toLowerCaseIfIgnoreCase(final String value, final boolean ignoreCase) {
        return ignoreCase ? value.toLowerCase() : value;
    }
}