package com;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lab.JaccardScorer;

public class JaccardScorerTest {

    private final JaccardScorer scorer = new JaccardScorer();
    private static final double DELTA = 1e-9; // Tolerance for floating-point comparison

    @Test
    @DisplayName("Partial intersection: {A, B, C} and {B, C, D} should yield a score of 0.5")
    public void testPartialIntersection() {
        Set<String> set1 = Set.of("A", "B", "C");
        Set<String> set2 = Set.of("B", "C", "D");
        double expected = 0.5;
        double actual = scorer.score(set1, set2);
        assertEquals(expected, actual, DELTA);
    }

    @Test
    @DisplayName("Two identical sets: {A, B, C} and {A, B, C} should yield a score of 1.0")
    public void testIdenticalSets() {
        Set<String> set1 = Set.of("A", "B", "C");
        Set<String> set2 = Set.of("A", "B", "C");
        double expected = 1.0;
        double actual = scorer.score(set1, set2);
        assertEquals(expected, actual, DELTA);
    }

    @Test
    @DisplayName("Two disjoint sets -> J = 0")
    void disjointSets_returnsZero() {
        double result = scorer.score(Set.of("TECH"), Set.of("ENERGY"));
        assertEquals(0.0, result, DELTA);
    }

    @Test
    @DisplayName("Two empty sets -> convention J = 1")
    void bothEmpty_returnsOneByConvention() {
        double result = scorer.score(Set.of(), Set.of());
        assertEquals(1.0, result, DELTA);
    }

    @Test
    @DisplayName("One empty set, one non-empty set -> J = 0")
    void oneEmptyOneNot_returnsZero() {
        double result = scorer.score(Set.of(), Set.of("TECH"));
        assertEquals(0.0, result, DELTA);
    }

    @Test
    @DisplayName("Intersection size 1, union size 3 -> J = 1/3, a repeating decimal (this is where delta actually matters)")
    void repeatingDecimalRatio_requiresDeltaToleranceToPass() {

        Set<String> event = Set.of("TECH", "ENERGY");
        Set<String> position = Set.of("TECH", "DEFENSE");

        double expected = 1.0 / 3.0;

        double result = scorer.score(event, position);
        assertEquals(expected, result, DELTA);

        double handRoundedExpected = 0.333333333; // One less digit
        double tightDelta = 1e-12; // Much smaller than difference between hand-rounded and true 1/3

        assertNotEquals(handRoundedExpected, result, tightDelta);

    }
    
}
