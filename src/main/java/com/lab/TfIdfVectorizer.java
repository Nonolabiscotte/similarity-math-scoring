package com.lab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TfIdfVectorizer {
    
    private final List<String> vocabulary;
    private final Map<String, Double> idfByTerm;

    public TfIdfVectorizer(List<List<String>> corpus) {
        this.vocabulary = buildVocabulary(corpus);
        this.idfByTerm = computeIdf(corpus, this.vocabulary);
    }

    public List<String> vocabulary() {
        return vocabulary;
    }

    public double[] vectorize(List<String> documentTokens) {
        Map<String, Long> termCounts = countTerms(documentTokens);
        long totalTerms = documentTokens.size();

        double[] vector = new double[vocabulary.size()];

        for (int i = 0; i < vocabulary.size(); i++) {
            String term = vocabulary.get(i);
            long count = termCounts.getOrDefault(term, 0L);

            if (count == 0 || totalTerms == 0) {
                vector[i] = 0.0;
                continue;
            }

            double tf = (double) count / totalTerms;
            double idf = idfByTerm.getOrDefault(term, 0.0);
            vector[i] = tf * idf;
        }

        return vector;
    }

    private List<String> buildVocabulary(List<List<String>> corpus) {
        Set<String> uniqueTerms = new HashSet<>();
        for (List<String> document : corpus) {
            uniqueTerms.addAll(document);
        }
        return new ArrayList<>(uniqueTerms).stream().sorted().toList();
    }

    private Map<String, Double> computeIdf(List<List<String>> corpus, List<String> vocabulary) {
        
        int totalDocuments = corpus.size();
        Map<String, Double> idf = new HashMap<>();
        
        for (String term : vocabulary) {
            long documentsContainingTerm = corpus.stream()
                .filter(document -> document.contains(term))
                .count();

            idf.put(term, Math.log((double) totalDocuments / documentsContainingTerm));
        }

        return idf;
    }

    private Map<String, Long> countTerms(List<String> documentTokens) {
        Map<String, Long> termCounts = new HashMap<>();

        for (String token : documentTokens) {
            termCounts.merge(token, 1L, Long::sum);
        }

        return termCounts;
    }
}
