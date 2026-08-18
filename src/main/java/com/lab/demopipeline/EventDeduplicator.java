package com.lab.demopipeline;

import java.util.ArrayList;
import java.util.List;

import com.lab.CosineSimilarity;
import com.lab.TfIdfVectorizer;
import com.lab.Tokenizer;

/**
 * EventDeduplicator
 */
public class EventDeduplicator {

    private final Tokenizer tokenizer = new Tokenizer();
    private final CosineSimilarity cosine = new CosineSimilarity();
    private final TfIdfVectorizer vectorizer;
    private final double similarityThreshold;

    private final List<double[]> canonicalVectors = new ArrayList<>();
    private final List<String> canonicalTexts = new ArrayList<>();

    public EventDeduplicator(List<List<String>> corpus, double similarityThreshold) {
        this.vectorizer = new TfIdfVectorizer(corpus);
        this.similarityThreshold = similarityThreshold;
    }

    public int addAndCheck(String rawText) {
        List<String> tokens = tokenizer.tokenize(rawText);
        double[] vector = vectorizer.vectorize(tokens);

        for (int i = 0; i < canonicalVectors.size(); i++) {
            double similarity = cosine.score(vector, canonicalVectors.get(i));

            if (similarity >= similarityThreshold) return 1;
        }

        canonicalVectors.add(vector);
        canonicalTexts.add(rawText);
        return canonicalVectors.size() - 1;
    }

    public List<String> canonicalTexts() {
        return canonicalTexts;
    }

}