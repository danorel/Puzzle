package db;

import java.util.Arrays;
import java.util.HashSet;

public class Pattern {
    private HashSet<String> sequence;
    private char delimiter;

    public Pattern(String sequence) {
        this(sequence, ',');
    }

    public Pattern(String sequence, char delimiter) {
        String[] tokens = sequence.split(String.valueOf(delimiter));
        this.sequence = new HashSet<>();
        this.sequence.addAll(Arrays.asList(tokens));
        this.delimiter = delimiter;
    }

    public boolean contains(String token) {
        return sequence.contains(token);
    }

    public boolean contains(char symbol) {
        return sequence.stream().anyMatch(sequenceToken -> sequenceToken.equals(String.valueOf(symbol)));
    }

    public boolean intersects(Pattern that) {
        return sequence.stream().anyMatch((that::contains));
    }

    public String encrypt(String sequence) {
        String[] tokens = sequence.split(String.valueOf(delimiter));
        StringBuilder message = new StringBuilder();
        for (String token : tokens) {
            if (token.equals("0") || this.contains(token)) {
                message.append(token).append(delimiter);
            } else {
                message.append("*").append(delimiter);
            }
        }
        return message.toString();
    }

    @Override
    public String toString() {
        return "Pattern{" +
                "sequence=" + sequence +
                "delimiter=" + delimiter +
                '}';
    }
}
