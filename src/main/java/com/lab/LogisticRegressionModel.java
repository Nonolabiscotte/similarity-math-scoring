package com.lab;

public class LogisticRegressionModel {
    
    private final double[] weights;

    public LogisticRegressionModel(double[] weights) {
        this.weights = weights;
    }

    public double predict(double[] features) {
        if (features.length != weights.length - 1) {
            throw new IllegalArgumentException(
                "Expected " + (weights.length - 1) + " features to match "
                            + weights.length + " weights (including bias), got " + features.length);
        }

        double z = logit(features);

        return Sigmoid.apply(z);
        
    }

    public double logit(double[] features) {
        double z = weights[0];

        for (int i = 0; i < features.length; i++) {
            z += weights[i + 1] * features[i];
        }

        return z;
    }
}
