package com.lab;

import java.util.HashSet;
import java.util.Set;

public class JaccardScorer {

    public double score(Set<String> set1, Set<String> set2) {
        if (set1.isEmpty() && set2.isEmpty()) return 1.0; // Both sets are empty, define similarity as 1

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }
}
