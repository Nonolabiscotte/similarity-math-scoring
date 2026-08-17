package com.lab;

public class Sigmoid {

    public static double apply(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }
    
}
