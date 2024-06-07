package de.unistuttgart.iste.meitrex.common.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class MeitrexCollectionUtilsTest {

    @Nested
    class DistinctByKeyTest {
        @Test
        void test_filters_out_duplicate_elements_based_on_unique_key() {
            // Arrange
            List<String> input = Arrays.asList("apple", "banana", "apple", "orange", "banana");
            Function<String, String> keyExtractor = Function.identity();

            // Act
            List<String> result = input.stream()
                    .filter(MeitrexCollectionUtils.distinctByKey(keyExtractor))
                    .toList();

            // Assert
            assertThat(result, contains("apple", "banana", "orange"));
        }

        @Test
        void test_filters_out_duplicate_elements_based_on_unique_key_with_custom_key_extractor() {
            // Arrange
            List<String> input = Arrays.asList("apple", "banana", "apple", "orange", "banana");
            Function<String, Integer> keyExtractor = String::length;

            // Act
            List<String> result = input.stream()
                    .filter(MeitrexCollectionUtils.distinctByKey(keyExtractor))
                    .toList();

            // Assert
            assertThat(result, contains("apple", "banana"));
        }

        @Test
        void test_handles_null_elements() {
            // Arrange
            List<String> input = Arrays.asList("apple", null, "apple", "orange", null);
            Function<String, String> keyExtractor = Function.identity();

            // Act
            List<String> result = input.stream()
                    .filter(MeitrexCollectionUtils.distinctByKey(keyExtractor))
                    .toList();

            // Assert
            assertThat(result, contains("apple", null, "orange"));
        }

    }

    @Nested
    class ReplaceContentOfListTest {

        @Test
        void test_replaces_content_of_non_null_list_with_new_content() {
            // Arrange
            List<String> originalList = new ArrayList<>(Arrays.asList("a", "b", "c"));
            List<String> newList = Arrays.asList("x", "y", "z");

            // Act
            List<String> result = MeitrexCollectionUtils.replaceContent(originalList, newList);

            // Assert
            assertThat(result, contains("x", "y", "z"));
            assertThat(result, sameInstance(originalList));
        }

        @Test
        void test_handles_non_empty_original_list_and_empty_new_content() {
            // Arrange
            List<String> originalList = new ArrayList<>(Arrays.asList("a", "b", "c"));
            List<String> newList = Collections.emptyList();

            // Act
            List<String> result = MeitrexCollectionUtils.replaceContent(originalList, newList);

            // Assert
            assertThat(result, is(empty()));
            assertThat(result, sameInstance(originalList));
        }

        @Test
        void test_creates_new_list_if_original_list_is_null() {
            // Arrange
            List<String> newList = Arrays.asList("x", "y", "z");

            // Act
            List<String> result = MeitrexCollectionUtils.replaceContent(null, newList);

            // Assert
            assertThat(result, notNullValue());
            assertThat(result, contains("x", "y", "z"));
        }
    }

    @Nested
    class ReplaceContentOfSetTest {

        @Test
        void test_replaces_content_of_non_null_set_with_new_content() {
            // Arrange
            Set<String> originalSet = new HashSet<>(Arrays.asList("a", "b", "c"));
            Set<String> newSet = Set.of("x", "y", "z");

            // Act
            Set<String> result = MeitrexCollectionUtils.replaceContent(originalSet, newSet);

            // Assert
            assertThat(result, containsInAnyOrder("x", "y", "z"));
            assertThat(result, sameInstance(originalSet));
        }

        @Test
        void test_handles_non_empty_original_set_and_empty_new_content() {
            // Arrange
            Set<String> originalSet = new HashSet<>(Arrays.asList("a", "b", "c"));
            Set<String> newSet = Collections.emptySet();

            // Act
            Set<String> result = MeitrexCollectionUtils.replaceContent(originalSet, newSet);

            // Assert
            assertThat(result, is(empty()));
            assertThat(result, sameInstance(originalSet));
        }

        @Test
        void test_creates_new_set_if_original_set_is_null() {
            // Arrange
            Set<String> newSet = Set.of("x", "y", "z");

            // Act
            Set<String> result = MeitrexCollectionUtils.replaceContent(null, newSet);

            // Assert
            assertThat(result, notNullValue());
            assertThat(result, containsInAnyOrder("x", "y", "z"));
        }

    }
    // note: Methods filter, map, concat, arrayListOf are not tested because they are trivial shortcuts for stream operations

}
