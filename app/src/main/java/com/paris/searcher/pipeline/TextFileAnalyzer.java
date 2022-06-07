package com.paris.searcher.pipeline;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class TextFileAnalyzer implements Runnable {
    private final String filePath;
    private final ConcurrentHashMap<String, TreeSet<String>> repository;

    public TextFileAnalyzer(String filePath, ConcurrentHashMap<String, TreeSet<String>> repository) {
        this.filePath = filePath;
        this.repository = repository;
    }

    @Override
    public void run() {
        try {
            //https://papaprimerizo.es/java-expresion-regular-para-validar-acentos-y-n/
            String content = Files.readString(Paths.get(filePath),StandardCharsets.UTF_8);
            content = content
                    .replaceAll("[^a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ ]", " ")
                    .replaceAll(" ","><")
                    .replaceAll("<>","")
                    .replaceAll("><"," ")
                    .toLowerCase()
                    .trim();

            String[] split = content.split(" ");
            List<String> terms = Arrays.asList(split);
            TreeSet<String> words = new TreeSet<>(terms);
            repository.put(filePath,words);
        } catch (IOException e) {
            System.out.println("Invalid file " + filePath);
        }
    }
}
