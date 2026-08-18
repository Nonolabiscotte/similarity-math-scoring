package com.lab;

import java.util.List;

public class LogisticRegressionTrainer {

    private final Sigmoid sigmoid = new Sigmoid();
    
    public double[] train(List<TrainingExample> examples, int iterations, double learningRate) {

        if (examples.isEmpty()) {
            throw new IllegalArgumentException("Cannot train on an empty dataset");
        }

        int featureCount = examples.get(0).features().length;

        // weights[0] = bias w0, weights[1 .. featureCount] = w1 .. wn
        // Starting  at all zeros: with no info yet, predict() would give 0.5
        // ==> neutral sigmoid proba sigmoid(0) = 0.5
        double[] weights = new double[featureCount + 1];

        for (int iteration = 0; iteration < iterations; iteration++) {
            double[] gradients = computeGradients(examples, weights, featureCount);

            for (int j = 0; j < weights.length; j++) {
                weights[j] -= learningRate * gradients[j];
            }
        }
        
        return weights;
    }

    private double[] computeGradients(List<TrainingExample> examples, double[] weights, int featureCount) {

        double[] gradients = new double[weights.length];

        for (TrainingExample example : examples) {
            double pHat = predictWithCurrentWeights(example.features(), weights);
            double error = pHat - example.label(); // (p_hat_i - y_i)

            gradients[0] += error; // bias term: its feature is implicilty 1

            for (int j = 0; j < featureCount; j++) {
                gradients[j + 1] += error * example.features()[j];
            }
        }

        return gradients;

    }

    private double predictWithCurrentWeights(double[] features, double[] weights) {
        double z = weights[0];

        for (int j = 0; j < features.length; j++) {
            z += weights[j + 1] * features[j];
        }

        return sigmoid.apply(z);
    }
}
