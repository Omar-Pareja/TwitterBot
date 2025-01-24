package org.cis1200;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class TwitterBot {

    /** The MarkovChain you'll be using to generate tweets */
    private final MarkovChain mc;

    /**
     * @param trainingData - cleaned data from which to construct the TwitterBot
     *                     MarkovModel
     */
    public TwitterBot(List<List<String>> trainingData) {
        mc = new MarkovChain(trainingData);
    }

    
    public String generateTweet(NumberGenerator ng) {
        StringBuilder tweet = new StringBuilder();
        Iterator<String> walk = mc.getWalk(ng); // Perform a walk through the MarkovChain

        while (walk.hasNext()) {
            String token = walk.next();

            // Add token to the tweet; avoid spaces before punctuation
            if (tweet.length() > 0 && !token.matches(TweetParser.PUNCTUATION_TOKEN)) {
                tweet.append(" ");
            }
            tweet.append(token);
        }

        return tweet.toString(); // Return the generated tweet as a String
    }


    public String generateTweet() {
        return generateTweet(new RandomNumberGenerator());
    }

    public List<String> generateRandomTweets(int numTweets) {
        List<String> tweets = new ArrayList<>();
        while (numTweets > 0) {
            tweets.add(generateTweet());
            numTweets--;
        }
        return tweets;
    }

}
