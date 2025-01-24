package org.cis1200;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class LineIterator implements Iterator<String> {

    private final BufferedReader reader;
    private String nextLine;
    private boolean hasErrorOccurred;

   
    public LineIterator(BufferedReader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("BufferedReader cannot be null.");
        }
        this.reader = reader;
        advance();
    }

    /**
     * Advances to the next line in the BufferedReader.
     */
    private void advance() {
        try {
            nextLine = reader.readLine();
            if (nextLine == null) {
                reader.close();
            }
        } catch (Exception e) {
            nextLine = null;
            hasErrorOccurred = true;
            try {
                reader.close();
            } catch (Exception closeException) {
            }
        }
    }

    public LineIterator(String filePath) {
        this(FileUtilities.fileToReader(filePath));
    }

    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    @Override
    public String next() {
        if (nextLine == null) {
            throw new NoSuchElementException("No more lines to read.");
        }
        String currentLine = nextLine;
        advance();
        return currentLine;
    }
}
