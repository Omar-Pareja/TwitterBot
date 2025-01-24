package org.cis1200;

import java.util.*;


public class MarkovChain {

    /** probability distribution of initial tokens in a sentence */
    final ProbabilityDistribution<String> startTokens;

    /** for each token, probability distribution of next token in a sentence */
    final Map<String, ProbabilityDistribution<String>> bigramFrequencies;

    /** end of sentence marker */
    static final String END_TOKEN = "<END>";

    /**
     * Construct an empty {@code MarkovChain} that can later be trained.
     *
     * This constructor is implemented for you.
     */
    public MarkovChain() {
        this.bigramFrequencies = new TreeMap<>();
        this.startTokens = new ProbabilityDistribution<>();
    }

    public MarkovChain(List<List<String>> trainingData) {
        this.bigramFrequencies = new TreeMap<>();
        this.startTokens = new ProbabilityDistribution<>();

        if (trainingData == null) {
            throw new IllegalArgumentException("Training data cannot be null.");
        }
        for (List<String> sequence : trainingData) {
            if (sequence == null) {
                throw new IllegalArgumentException("A sequence in the training data is null.");
            }
            addSequence(sequence.iterator());
        }
    }

    void addBigram(String first, String second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Tokens cannot be null");
        }
        ProbabilityDistribution<String> distribution =
                bigramFrequencies.computeIfAbsent(first, k -> new ProbabilityDistribution<>());
        distribution.record(second);
    }

    public void addSequence(Iterator<String> tokens) {
        if (!tokens.hasNext()) {
            return;
        }

        String first = tokens.next();
        startTokens.record(first);

        while (tokens.hasNext()) {
            String second = tokens.next();
            addBigram(first, second);
            first = second;
        }

        addBigram(first, END_TOKEN);
    }


    ProbabilityDistribution<String> get(String token) {
        if (token == null) {
            throw new IllegalArgumentException("token cannot be null.");
        }
        return bigramFrequencies.get(token);
    }

  
    public Iterator<String> getWalk(NumberGenerator ng) {
        return new MarkovChainIterator(startTokens, bigramFrequencies, ng);
    }

  
    public Iterator<String> getRandomWalk() {
        return getWalk(new RandomNumberGenerator());
    }


    public List<Integer> findWalkChoices(List<String> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            throw new IllegalArgumentException("Invalid empty or null tokens");
        }
        tokens.add(MarkovChain.END_TOKEN);
        List<Integer> choices = new LinkedList<>();

        String curToken = tokens.remove(0);
        choices.add(startTokens.index(curToken));

        while (tokens.size() > 0) {
            ProbabilityDistribution<String> curDist = bigramFrequencies.get(curToken);
            String nextToken = tokens.remove(0);
            choices.add(curDist.index(nextToken));
            curToken = nextToken;
        }
        return choices;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("startTokens: ").append(startTokens.toString());
        res.append("\nbigramFrequencies:\n");
        for (Map.Entry<String, ProbabilityDistribution<String>> c : bigramFrequencies.entrySet()) {
            res.append("\"");
            res.append(c.getKey());
            res.append("\":\t");
            res.append(c.getValue().toString());
            res.append("\n");
        }
        return res.toString();
    }
}
