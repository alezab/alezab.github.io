package com.example;

import java.io.*;
import java.util.*;

public class FileHandler {
    public static void printFileLines(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                splitLine(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
    private static final String DELIMITER = ","; // przyjrzyjcie sie dokładnie jaki jest separator w pliku .csv (zazwyczaj "," ";" "|")
    
    public static void splitLine(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, DELIMITER);

        // tokenizer działa podobnie jak iterator
        while (tokenizer.hasMoreTokens()) {
            System.out.println(tokenizer.nextToken());
        }
    }
}
