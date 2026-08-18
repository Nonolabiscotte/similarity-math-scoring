package com.lab.demopipeline;

import java.util.List;
import java.util.Set;

import com.lab.LogisticRegressionModel;
import com.lab.LogisticRegressionTrainer;
import com.lab.RelevanceScorer;
import com.lab.Tokenizer;
import com.lab.TrainingExample;

/**
 * FullPipelineDemo
 */
public class FullPipelineDemo {

    public static void main(String[] args) {


        // =====================================================================
        // STEP 1: DEDUPLICATION -- three sources report events, some duplicates,
        // some not. This also honestly demonstrates a known TF-IDF limitation.
        // =====================================================================
        List<String> rawEventTexts = List.of(
            "The Fed raises interest rates by a quarter point",                  // source A
            "The Fed raises interest rates by a quarter point again this year",  // source A', close paraphrase -> WILL be caught (shared words)
            "Federal Reserve hikes its benchmark rate 25 basis points",           // source B, true synonym paraphrase -> will NOT be caught (see note below)
            "Tech stocks rally after strong earnings reports"                   // a completely different event
        );

        Tokenizer tokenizer = new Tokenizer();
        List<List<String>> tokenizedCorpus = rawEventTexts.stream()
            .map(tokenizer::tokenize).toList();

        EventDeduplicator deduplicator = new EventDeduplicator(tokenizedCorpus, 0.4);

        System.out.println("====Deduplication====");

        for (String rawText : rawEventTexts) {
            int canonicalIndex = deduplicator.addAndCheck(rawText);
            System.out.println(System.out.printf("\"%s\" -> canonical event #%d%n", rawText, canonicalIndex));
        }

        System.out.println("Total distinct canonical events after dedup: "
                + deduplicator.canonicalTexts().size());
        System.out.println("NOTE: sources A and A' merge (they share most of their exact words).");
        System.out.println("Source B does NOT merge with A, even though it describes the SAME event:");
        System.out.println("\"hikes\"/\"benchmark\"/\"rate\" share zero exact tokens with \"raises\"/\"interest\"/\"rates\".");
        System.out.println("This is the TF-IDF limitation predicted earlier in theory: exact-word matching only,");
        System.out.println("no notion of synonyms. Fixing this for real would require swapping TfIdfVectorizer");


        // =====================================================================
        // STEP 2: OFFLINE TRAINING -- learn weights from historical feedback.
        // (In the real system, these rows come from the ScoreFeedback table.)
        // =====================================================================
        List<TrainingExample> history = List.of(
                new TrainingExample(new double[]{0.9, 0.7, 0.2}, 1),
                new TrainingExample(new double[]{0.8, 0.6, 0.1}, 1),
                new TrainingExample(new double[]{0.7, 0.5, 0.0}, 1),
                new TrainingExample(new double[]{0.6, 0.4, 0.3}, 1),
                new TrainingExample(new double[]{0.1, 0.1, 0.0}, 0),
                new TrainingExample(new double[]{0.0, 0.2, 0.1}, 0),
                new TrainingExample(new double[]{0.2, 0.0, 0.0}, 0),
                new TrainingExample(new double[]{0.1, 0.1, 0.2}, 0)
        );

        LogisticRegressionTrainer trainer = new LogisticRegressionTrainer();
        double[] learnedWeights = trainer.train(history, 5000, 0.5);
        LogisticRegressionModel model = new LogisticRegressionModel(learnedWeights);

        System.out.println("=== Training ===");
        System.out.printf("Learned weights: w0=%.3f w1=%.3f w2=%.3f w3=%.3f%n%n",
                learnedWeights[0], learnedWeights[1], learnedWeights[2], learnedWeights[3]);

        // =====================================================================
        // STEP 3: RELEVANCE SCORING -- the first canonical event (the Fed rate
        // hike, deduplicated from 2 sources) scored against two positions.
        // Manual tagging here: extracting these tags automatically from text is
        // a separate rule-based/lexicon step, not covered by this lab.
        // =====================================================================
        RelevanceScorer scorer = new RelevanceScorer(model);

        Set<String> eventFactors = Set.of("INTEREST_RATE_SENSITIVITY", "USD_EXPOSURE");
        Set<String> eventSectors = Set.of();
        Set<String> eventGeographies = Set.of("US");

        Set<String> bondPositionFactors = Set.of("INTEREST_RATE_SENSITIVITY", "INFLATION_SENSITIVITY");
        Set<String> bondPositionSectors = Set.of();
        Set<String> bondPositionGeographies = Set.of("US", "EU");

        Set<String> emPositionFactors = Set.of("GEOPOLITICAL_RISK");
        Set<String> emPositionSectors = Set.of("TECH");
        Set<String> emPositionGeographies = Set.of("EMERGING");

        double bondRelevance = scorer.scoreRelevance(
                eventFactors, bondPositionFactors,
                eventSectors, bondPositionSectors,
                eventGeographies, bondPositionGeographies);

        double emRelevance = scorer.scoreRelevance(
                eventFactors, emPositionFactors,
                eventSectors, emPositionSectors,
                eventGeographies, emPositionGeographies);

        System.out.println("=== Relevance scoring ===");
        System.out.println("Canonical event scored: \"" + deduplicator.canonicalTexts().get(0) + "\"");
        System.out.printf("Relevance for the bond-heavy position: %.1f%%%n", bondRelevance * 100);
        System.out.printf("Relevance for the EM equity position:  %.1f%%%n", emRelevance * 100);
        
    }
}