package com.cryptoportfolio.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CsvReader {

    public static List<String[]> readCsv(String filePath) throws IOException {
        return readCsv(filePath, true);
    }

    public static List<String[]> readCsv(String filePath, boolean skipHeader) throws IOException {
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + filePath);
        }

        List<String> lines = Files.readAllLines(path);
        
        if (skipHeader && !lines.isEmpty()) {
            lines = lines.subList(1, lines.size());
        }

        return lines.stream()
            .map(line -> line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)) // Handles quoted values
            .map(row -> {
                for (int i = 0; i < row.length; i++) {
                    row[i] = row[i].trim();
                    if (row[i].startsWith("\"") && row[i].endsWith("\"")) {
                        row[i] = row[i].substring(1, row[i].length() - 1);
                    }
                }
                return row;
            })
            .collect(Collectors.toList());
    }
}