package com.paris.searcher.processor;

import java.util.TreeSet;

public class SearchAnalyzer implements Runnable {
    private final String filePath;
    private final TreeSet<String> wordsFromDoc;
    private final TreeSet<String> wordsToLookFor;
    private int score;

    public SearchAnalyzer(String filePath, TreeSet<String> wordsFromDoc, TreeSet<String> wordsToLookFor) {
        this.filePath = filePath;
        this.wordsFromDoc = wordsFromDoc;
        this.wordsToLookFor = wordsToLookFor;
        this.score = 0;
    }

    @Override
    public void run() {
        int count = 0;
        for(String w : wordsToLookFor) {
            if(wordsFromDoc.contains(w))
                count++;
        }

        double perc = 100 * (double) count / (double) wordsToLookFor.size();
        score = (int) Math.round(perc);
    }

    public int getScore() {
        return score;
    }

    public String getFileName() {
        String[] chunks = filePath.split("/");
        return chunks[chunks.length-1];
    }
}
