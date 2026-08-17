package com;

import org.junit.jupiter.api.Test;

import com.lab.CosineSimilarity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;

public class CosineSimilarityTest {
    
    private final CosineSimilarity scorer = new CosineSimilarity();
    private static final double DELTA = 1e-9; // Tolerance for floating-point comparison

    @Test
    @DisplayName("dot: sum of pairwise products of two vectors")
    void dot_computesSumOfPairwiseProducts() {
        double[] vectorA = {1.0, 2.0, 3.0};
        double[] vectorB = {4.0, 5.0, 6.0};
        double expectedDotProduct = 32.0; // 1*4 + 2*5 + 3*6
        double actualDotProduct = scorer.score(vectorA, vectorB) * (scorer.norm(vectorA) * scorer.norm(vectorB));
        assertEquals(expectedDotProduct, actualDotProduct, DELTA);
    }

    @Test
    @DisplayName("dot: orthogonal vectors should yield a dot product of 0")
    void dot_orthogonalVectors_returnsZero() {
        double[] vectorA = {1.0, 0.0};
        double[] vectorB = {0.0, 1.0};
        double expectedDotProduct = 0.0;
        double actualDotProduct = scorer.dot(vectorA, vectorB);
        assertEquals(expectedDotProduct, actualDotProduct, DELTA);  
    }

    @Test
    @DisplayName("dot: opposite vectors should yield a negative dot product")
    void dot_oppositeVectors_returnsNegative() {
        double[] vectorA = {1.0, 2.0};
        double[] vectorB = {-1.0, -2.0};
        double expectedDotProduct = -5.0; // 1*(-1) + 2*(-2)
        double actualDotProduct = scorer.dot(vectorA, vectorB);
        assertEquals(expectedDotProduct, actualDotProduct, DELTA);
    }

    @Test
    @DisplayName("dot: is commutative, dot(a,b) == dot(b,a)")
    void dot_isCommutative() {
        double[] a = {1.0, 2.0, 3.0};
        double[] b = {4.0, -5.0, 6.0};

        double ab = scorer.dot(a, b);
        double ba = scorer.dot(b, a);

        assertEquals(ab, ba, DELTA);
    }
    
}
