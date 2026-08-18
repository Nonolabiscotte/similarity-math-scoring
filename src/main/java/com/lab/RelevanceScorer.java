package com.lab;

import java.util.Set;

public class RelevanceScorer {
    
    private final JaccardScorer jaccard = new JaccardScorer();
    private final LogisticRegressionModel model;

    public RelevanceScorer(LogisticRegressionModel model) {
        this.model = model;
    }

    public double scoreRelevance(
            Set<String> eventFactors, Set<String> exposureFactors,
            Set<String> eventSectors, Set<String> exposureSectors,
            Set<String> eventGeographies, Set<String> exposureGeographies) {

        double factorMatch = jaccard.score(eventFactors, exposureFactors);
        double sectorMatch = jaccard.score(eventSectors, exposureSectors);
        double geoMatch = jaccard.score(eventGeographies, exposureGeographies);

        double[] features = {factorMatch, sectorMatch, geoMatch};

        return model.predict(features);
    }
}
