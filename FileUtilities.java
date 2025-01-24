package org.cis1200;

import java.io.*;
import java.util.List;

/**
 * This class organizes some static methods for working with File IO.
 */
public class FileUtilities {

    public static BufferedReader fileToReader(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null.");
        }
        try {
            return new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found: " + filePath, e);
        }
    }

    public static void writeStringsToFile(List<String> strings, String filePath, boolean append) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null.");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
            for (String line : strings) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error writing to file: " + filePath, e);
        }
    }
}
