package io.quell.techtest.domain;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.quell.techtest.helpers.IdentityHelper.ALL_ROYAL_FLUSH_HANDS;
import static org.assertj.core.api.Assertions.assertThat;

@FieldDefaults(level = AccessLevel.PRIVATE)
class HandTests {

    // unlike the other two pojo classes, Hand has two convenience methods to extract the Rank or Suit values, which need tests

    @ParameterizedTest
    @MethodSource({"buildHandRanks_ResultsAndExpectations", "buildHandSuits_ResultsAndExpectations"})
    void test_GetHandRanks_And_GetHandSuits(List<String> cutResults, List<String> expected) {
        assertThat(cutResults).hasSameElementsAs(expected);
    }

    // inject Rank functions
    static Stream<Arguments> buildHandRanks_ResultsAndExpectations() {
        return runMatchedFunctionsBuildingClassUnderTestResultsAndExpectations(Card::getRank, Hand::getHandRanks);
    }

    // inject Suit functions
    static Stream<Arguments> buildHandSuits_ResultsAndExpectations() {
        return runMatchedFunctionsBuildingClassUnderTestResultsAndExpectations(Card::getSuit, Hand::getHandSuits);
    }

    // code can now be common thanks to function injection
    static Stream<Arguments> runMatchedFunctionsBuildingClassUnderTestResultsAndExpectations(Function<Card, String> rankOrSuitFn,
                                                                                             Function<Hand, List<String>> classUnderTestFn) {
        return ALL_ROYAL_FLUSH_HANDS.stream()
                .map(rf -> {
                    List<String> result = executeClassUnderTestMethod(classUnderTestFn, rf);
                    List<String> expected = rf.stream().map(rankOrSuitFn).collect(Collectors.toList());
                    return Arguments.of(result, expected);
                });
    }

    private static List<String> executeClassUnderTestMethod(Function<Hand, List<String>> classUnderTestMethod, List<Card> royalFlush) {
        Hand classUnderTest = new Hand(new HashSet<>(royalFlush));

        return Stream.of(classUnderTest)
                .map(classUnderTestMethod)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}