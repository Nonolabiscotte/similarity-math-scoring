package com.lab;

public class CosineSimilarity {
    
    public double score(double[] vectorA, double[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("Vectors must be of the same length");
        }

        double dotProduct = dot(vectorA, vectorB);
        double normA = norm(vectorA);
        double normB = norm(vectorB);

        if (normA == 0.0 || normB == 0.0) {
            return 0.0; // If either vector is zero, cosine similarity is defined as 0
        }

        // cosine similarity is the dot product divided by the product of the norms
        return dotProduct / (normA * normB);
    }

    /**
     * Calculates the dot product of two vectors.
     * @param vectorA
     * @param vectorB
     * @return
     */
    public double dot(double[] vectorA, double[] vectorB) {
        double sum = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            sum += vectorA[i] * vectorB[i];
        }
        return sum;
    }

    /**
     * Calculates the Euclidean norm (magnitude) of a vector.
     * @param vector
     * @return
     */
    public double norm(double[] vector) {
        double sumOfSquares = 0.0;
        for (double dim : vector) {
            sumOfSquares += dim * dim;
        }
        return Math.sqrt(sumOfSquares);
    }
}
