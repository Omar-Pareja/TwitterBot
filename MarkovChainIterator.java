package org.cis1200;

import java.util.*;


class MarkovChainIterator implements Iterator<String> {

    // The iterator needs to have references to the data stored in the MarkovChain
    // the distribution of the startTokens and the bigramFrequencies. These are
    private final ProbabilityDistribution<String> startTokens;
    private final Map<String, ProbabilityDistribution<String>> bigramFrequencies;

    // stores the source of numbers that determine the path of ths walk
    private final NumberGenerator ng;
    private String currentToken;
    private boolean hasMoreTokens;

  
    MarkovChainIterator(
            ProbabilityDistribution<String> startTokens,
            Map<String, ProbabilityDistribution<String>> bigramFrequencies,
            NumberGenerator ng
    ) {
        this.startTokens = startTokens;
        this.bigramFrequencies = bigramFrequencies;
        this.ng = ng;
        try {
            this.currentToken = startTokens.pick(ng);
            this.hasMoreTokens = true;
        } catch (IllegalArgumentException | NoSuchElementException e) {
            this.currentToken = null;
            this.hasMoreTokens = false;
        }
    }

   
   
    @Override
    public boolean hasNext() {
        return hasMoreTokens && !MarkovChain.END_TOKEN.equals(currentToken);
    }

   
    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more tokens in the walk.");
        }

        String tokenToReturn = currentToken;

        try {
            ProbabilityDistribution<String> nextDistribution = bigramFrequencies.get(currentToken);

            if (nextDistribution == null) {
                currentToken = MarkovChain.END_TOKEN;
                hasMoreTokens = false;
            } else {
                currentToken = nextDistribution.pick(ng);
            }
        } catch (IllegalArgumentException | NoSuchElementException e) {
            currentToken = MarkovChain.END_TOKEN;
            hasMoreTokens = false;
        }

        return tokenToReturn;
    }
}
