package com.lab;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public static List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        String[] rawParts = text.toLowerCase().split("[^\\p{L}\\p{N}]+");
        for (String part : rawParts) {
            if (!part.isEmpty()) tokens.add(part);
        }
        return tokens;
    }
}
