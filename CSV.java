package org.cis1200;

import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;



public class CSV {

    /**
     * Define constants for the "special" characters used in CSV files.
     */
    private final static char DOUBLE_QUOTES = '"';
    private final static char COMMA = ',';


    public static List<String> parseRecord(String csvLine) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean insideQuotes = false;
        for (int i = 0; i < csvLine.length(); i++) {
            char c = csvLine.charAt(i);

            if (insideQuotes) {
                if (c == '"') {

                    if (i + 1 < csvLine.length() && csvLine.charAt(i + 1) == '"') {
                        currentField.append('"');
                        i++;
                    } else {
                        insideQuotes = false;
                    }
                } else {
                    currentField.append(c);
                }
            } else {
                if (c == '"') {
                    insideQuotes = true;
                } else if (c == ',') {
                    fields.add(currentField.toString());
                    currentField.setLength(0);
                } else {
                    currentField.append(c);
                }
            }
        }

        fields.add(currentField.toString());
        return fields;
    }

    
    public static String extractColumn(String csvLine, int column) {
        if (csvLine == null) {
            throw new IllegalArgumentException("CSV line cannot be null.");
        }
        List<String> fields = parseRecord(csvLine);
        if (column < 0 || column >= fields.size()) {
            throw new IndexOutOfBoundsException("Column index out of range: " + column);
        }
        return fields.get(column);
    }

  
    public static List<String> csvFieldsAtColumn(BufferedReader reader, int column) {
        if (reader == null) {
            throw new IllegalArgumentException("BufferedReader cannot be null.");
        }
        List<String> columnData = new ArrayList<>();
        String csvLine;

        try {
            while ((csvLine = reader.readLine()) != null) {
                try {
                    columnData.add(extractColumn(csvLine, column));
                } catch (IndexOutOfBoundsException e) {
                    // Skip invalid rows for the specified column
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error reading from BufferedReader.", e);
        }

        return columnData;
    }

}
