package org.cis1200;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/** Tests for TwitterBot class */
public class TwitterBotTest {
    // A helper function for creating lists of strings
    private static List<String> listOfArray(String[] words) {
        List<String> l = new LinkedList<>();
        Collections.addAll(l, words);
        return l;
    }


    private static final String[] TWEET_1 = {"a", "table", "and", "a", "chair"};
    private static final String[] TWEET_2 = {"a", "banana", "!", "and", "a", "banana", "?"};

    private static List<List<String>> getTestTrainingDataExample() {
        List<List<String>> trainingData = new LinkedList<>();
        trainingData.add(listOfArray(TWEET_1));
        trainingData.add(listOfArray(TWEET_2));
        return trainingData;
    }

    @Test
    public void generatorWorks() {
        TwitterBot tb = new TwitterBot(getTestTrainingDataExample());

        String expected = "a banana?";
        int[] walk = {0, 0, 1, 0};
        NumberGenerator ng = new ListNumberGenerator(walk);
        assertEquals(expected, tb.generateTweet(ng));
    }


    @Test
    public void emptyTrainingDataCreatesEmptyTweet() {

        assertTimeoutPreemptively(
                Duration.ofSeconds(10), () -> {

                    TwitterBot tb = new TwitterBot(new LinkedList<>());

                    assertEquals(0, tb.generateTweet().length());
                }
        );
    }

    @Test
    public void testGenerateTweetFromFirstTweet() {
        TwitterBot tb = new TwitterBot(getTestTrainingDataExample());

        String expected = "a banana?";
        int[] walk = {0, 0, 1, 2, 0}; // Follows the first tweet entirely
        NumberGenerator ng = new ListNumberGenerator(walk);

        assertEquals(expected, tb.generateTweet(ng));
    }

    @Test
    public void testGenerateTweetWithPartialWalk() {
        TwitterBot tb = new TwitterBot(getTestTrainingDataExample());

        String expected = "a banana?";
        int[] walk = {0, 0, 1};
        NumberGenerator ng = new ListNumberGenerator(walk);

        assertEquals(expected, tb.generateTweet(ng));
    }

    @Test
    public void testGenerateTweetWithRepeatingTokens() {
        TwitterBot tb = new TwitterBot(getTestTrainingDataExample());

        String expected = "a banana?";
        int[] walk = {0, 0, 1, 0, 0, 1, 0};
        NumberGenerator ng = new ListNumberGenerator(walk);

        assertEquals(expected, tb.generateTweet(ng));
    }
}
    /**
     *
     * Tests generating a tweet using a walk that navigates through the first tweet's tokens.
         */

