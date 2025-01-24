package org.cis1200;

import org.junit.jupiter.api.Test;
import java.io.StringReader;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for LineIterator */
public class LineIteratorTest {

    /*
     * Here's a test to help you out, but you still need to write your own.
     */

    @Test
    public void testHasNextAndNext() {

        // Note we don't need to create a new file here in order to
        // test out our LineIterator if we do not want to. We can just
        // create a StringReader to make testing easy!
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        LineIterator li = new LineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("0, The end should come here.", li.next());
        assertTrue(li.hasNext());
        assertEquals("1, This comes from data with no duplicate words!", li.next());
        assertFalse(li.hasNext());
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */


    @Test
    public void testSingleLineInput() {
        StringReader sr = new StringReader("Only one line of text.");
        BufferedReader br = new BufferedReader(sr);
        LineIterator li = new LineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("Only one line of text.", li.next());
        assertFalse(li.hasNext());
    }

    @Test
    public void testMultipleEmptyLines() {
        StringReader sr = new StringReader("\n\n\n");
        BufferedReader br = new BufferedReader(sr);
        LineIterator li = new LineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("", li.next());
        assertTrue(li.hasNext());
        assertEquals("", li.next());
        assertTrue(li.hasNext());
        assertEquals("", li.next());
        assertFalse(li.hasNext());
    }

    @Test
    public void testClosingBufferedReader() {
        StringReader sr = new StringReader("Line 1\nLine 2");
        BufferedReader br = new BufferedReader(sr);
        LineIterator li = new LineIterator(br);
        while (li.hasNext()) {
            li.next();
        }
        assertDoesNotThrow(() -> br.close());
    }

}
