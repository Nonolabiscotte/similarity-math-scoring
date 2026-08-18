package com.lab.demopipeline;

import java.util.Set;

import com.lab.JaccardScorer;
import com.lab.LogisticRegressionModel;

public class RelevantScorer {

    private final JaccardScorer jaccard = new JaccardScorer();
    private final LogisticRegressionModel model;

    public RelevantScorer(LogisticRegressionModel model) {
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
