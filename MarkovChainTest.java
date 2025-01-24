package org.cis1200;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for MarkovChain */
public class MarkovChainTest {

   
    private static Set<String> singleton(String s) {
        Set<String> set = new TreeSet<>();
        set.add(s);
        return set;
    }

    
    @Test
    public void testIllustrativeExampleMarkovChain() {
        /*
         * Note: we provide the pre-parsed sequence of tokens.
         */
        String[] tweet1 = { "a", "table", "and", "a", "chair" };
        String[] tweet2 = { "a", "banana", "!", "and", "a", "banana", "?" };

        MarkovChain mc = new MarkovChain();
        mc.addSequence(Arrays.stream(tweet1).iterator());
        mc.addSequence(Arrays.stream(tweet2).iterator());

        // Print out the Markov chain
        System.out.println("ILLUSTRATIVE EXAMPLE MARKOV CHAIN:\n" + mc);

        ProbabilityDistribution<String> pdBang = mc.get("!");
        assertEquals(singleton("and"), pdBang.keySet());
        assertEquals(1, pdBang.count("and"));

        ProbabilityDistribution<String> pdQuestion = mc.get("?");
        assertEquals(singleton(MarkovChain.END_TOKEN), pdQuestion.keySet());
        assertEquals(1, pdQuestion.count(MarkovChain.END_TOKEN));

        assertEquals(2, mc.startTokens.getTotal());
        assertEquals(2, mc.startTokens.count("a"));
        ProbabilityDistribution<String> pdA = mc.get("a");
        Set<String> keysA = new TreeSet<>();
        keysA.add("banana");
        keysA.add("chair");
        keysA.add("table");
        assertEquals(keysA, pdA.keySet());
        assertEquals(2, pdA.count("banana"));
        assertEquals(1, pdA.count("chair"));
        assertEquals(1, pdA.count("table"));

        ProbabilityDistribution<String> pdAnd = mc.get("and");
        assertEquals(singleton("a"), pdAnd.keySet());
        assertEquals(2, pdAnd.count("a"));

        ProbabilityDistribution<String> pdBanana = mc.get("banana");
        Set<String> keysBanana = new TreeSet<>();
        keysBanana.add("!");
        keysBanana.add("?");
        assertEquals(keysBanana, pdBanana.keySet());
        assertEquals(1, pdBanana.count("!"));
        assertEquals(1, pdBanana.count("?"));

        ProbabilityDistribution<String> pdChair = mc.get("chair");
        assertEquals(singleton(MarkovChain.END_TOKEN), pdChair.keySet());
        assertEquals(1, pdChair.count(MarkovChain.END_TOKEN));

        ProbabilityDistribution<String> pdTable = mc.get("table");
        assertEquals(singleton("and"), pdTable.keySet());
        assertEquals(1, pdTable.count("and"));
    }

    @Test
    public void testAddBigramWithMultipleOccurrences() {

        MarkovChain mc = new MarkovChain();
        mc.addBigram("hello", "world");
        mc.addBigram("hello", "world");
        mc.addBigram("hello", "world");

        assertTrue(mc.bigramFrequencies.containsKey("hello"));
        ProbabilityDistribution<String> pd = mc.bigramFrequencies.get("hello");
        assertNotNull(pd);
        assertEquals(3, pd.count("world"));
    }

    @Test
    public void testAddBigramWithMultipleFollowers() {

        MarkovChain mc = new MarkovChain();
        mc.addBigram("start", "middle");
        mc.addBigram("start", "end");
        mc.addBigram("start", "middle");

        assertTrue(mc.bigramFrequencies.containsKey("start"));
        ProbabilityDistribution<String> pd = mc.bigramFrequencies.get("start");
        assertNotNull(pd);

        // Check frequencies of followers
        assertEquals(2, pd.count("middle"));
        assertEquals(1, pd.count("end"));
    }

    @Test
    public void testAddBigram() {
        MarkovChain mc = new MarkovChain();
        mc.addBigram("1", "2");
        assertTrue(mc.bigramFrequencies.containsKey("1"));
        ProbabilityDistribution<String> pd = mc.bigramFrequencies.get("1");
        assertTrue(pd.getRecords().containsKey("2"));
        assertEquals(1, pd.count("2"));
    }

    @Test
    public void testEmptyInput() {
        /*
         * Tests how the MarkovChain handles an empty input sequence.
         * The chain should not have any start tokens or bigrams.
         */
        MarkovChain mc = new MarkovChain();
        mc.addSequence(Collections.emptyIterator());

        assertEquals(0, mc.startTokens.getTotal());
        assertTrue(mc.bigramFrequencies.isEmpty());
    }

    @Test
    public void testSingleWordSequence() {
        /*
         * Tests adding a sequence that consists of a single token.
         * The chain should record the token as a start token and pair it with END_TOKEN.
         */
        MarkovChain mc = new MarkovChain();
        String sentence = "hello";
        mc.addSequence(Arrays.stream(sentence.split(" ")).iterator());

        assertEquals(1, mc.startTokens.getTotal());
        assertEquals(1, mc.startTokens.count("hello"));

        ProbabilityDistribution<String> pd = mc.get("hello");
        assertNotNull(pd);
        assertEquals(1, pd.count(MarkovChain.END_TOKEN));
    }

    @Test
    public void testMultipleSentencesWithOverlap() {
        /*
         * Tests adding multiple sentences that share overlapping tokens.
         * Ensures that the probabilities are calculated correctly.
         */
        MarkovChain mc = new MarkovChain();
        String sentence1 = "this is a test";
        String sentence2 = "this is another test";
        mc.addSequence(Arrays.stream(sentence1.split(" ")).iterator());
        mc.addSequence(Arrays.stream(sentence2.split(" ")).iterator());

        // Check start tokens
        assertEquals(2, mc.startTokens.getTotal());
        assertEquals(2, mc.startTokens.count("this"));

        // Check bigram frequencies for "is"
        ProbabilityDistribution<String> pdIs = mc.get("is");
        assertNotNull(pdIs);
        assertEquals(1, pdIs.count("a"));
        assertEquals(1, pdIs.count("another"));

        // Check bigram frequencies for "test"
        ProbabilityDistribution<String> pdTest = mc.get("test");
        assertNotNull(pdTest);
        assertEquals(2, pdTest.count(MarkovChain.END_TOKEN));
    }

    @Test
    public void testWalkGeneratesValidSequence() {
        /*
         * Tests if a walk through the Markov Chain generates a valid sequence.
         * Ensures the generated sequence follows the probabilities set up by the chain.
         */
        String sentence = "hello world!";
        MarkovChain mc = new MarkovChain();
        mc.addSequence(Arrays.stream(sentence.split(" ")).iterator());

        List<Integer> choices = Arrays.asList(0, 0, 0); // Picks "hello" -> "world!" -> <END>
        Iterator<String> walk = mc.getWalk(new ListNumberGenerator(choices));

        List<String> result = new ArrayList<>();
        while (walk.hasNext()) {
            result.add(walk.next());
        }

        assertEquals(List.of("hello", "world!"), result);
    }

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testAddSequence() {
        MarkovChain mc = new MarkovChain();
        String sentence = "1 2 3";
        mc.addSequence(Arrays.stream(sentence.split(" ")).iterator());
        assertEquals(3, mc.bigramFrequencies.size());
        ProbabilityDistribution<String> pd1 = mc.bigramFrequencies.get("1");
        assertTrue(pd1.getRecords().containsKey("2"));
        assertEquals(1, pd1.count("2"));
        ProbabilityDistribution<String> pd2 = mc.bigramFrequencies.get("2");
        assertTrue(pd2.getRecords().containsKey("3"));
        assertEquals(1, pd2.count("3"));
        ProbabilityDistribution<String> pd3 = mc.bigramFrequencies.get("3");
        assertTrue(pd3.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(1, pd3.count(MarkovChain.END_TOKEN));
    }

    
    @Test
    public void testWalk() {

        String[] expectedTokens = { "CIS", "1200", "beats", "CIS", "1200", "rocks" };
        MarkovChain mc = new MarkovChain();

        String sentence1 = "CIS 1200 rocks";
        String sentence2 = "CIS 1200 beats CIS 1600";
        mc.addSequence(Arrays.stream(sentence1.split(" ")).iterator());
        mc.addSequence(Arrays.stream(sentence2.split(" ")).iterator());

        // it can be illustrative to print out the state of the Markov Chain at this
        // point
        System.out.println(mc);

        Integer[] seq = { 0, 0, 0, 0, 0, 1, 0 };
        List<Integer> choices = new ArrayList<>(Arrays.asList(seq));
        Iterator<String> walk = mc.getWalk(new ListNumberGenerator(choices));
        for (String token : expectedTokens) {
            assertTrue(walk.hasNext());
            assertEquals(token, walk.next());
        }
        assertFalse(walk.hasNext());

    }

    @Test
    public void testWalk2() {
        /* We can also use the provided method */

        String[] expectedWords = { "CIS", "1600" };
        MarkovChain mc = new MarkovChain();

        String sentence1 = "CIS 1200 rocks";
        String sentence2 = "CIS 1200 beats CIS 1600";
        mc.addSequence(Arrays.stream(sentence1.split(" ")).iterator());
        mc.addSequence(Arrays.stream(sentence2.split(" ")).iterator());

        List<Integer> choices = mc.findWalkChoices(new ArrayList<>(Arrays.asList(expectedWords)));
        Iterator<String> walk = mc.getWalk(new ListNumberGenerator(choices));
        for (String word : expectedWords) {
            assertTrue(walk.hasNext());
            assertEquals(word, walk.next());
        }

    }

}
