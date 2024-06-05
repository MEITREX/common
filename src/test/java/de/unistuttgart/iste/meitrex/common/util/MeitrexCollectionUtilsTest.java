package de.unistuttgart.iste.meitrex.common.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

class MeitrexCollectionUtilsTest {

    // test for distinctByKey (other methods are not tested yet)

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
