package com.paris.searcher.processor;

import com.paris.searcher.pipeline.TextFileRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SearchScheduler {

    private final int threadCount;
    private final int resultCount;
    private final TextFileRepository textFileRepository;

    public SearchScheduler(Properties myProps, TextFileRepository textFileRepository) {
        this.threadCount = Integer.parseInt(myProps.getProperty("threadCount"));
        this.resultCount = Integer.parseInt(myProps.getProperty("resultCount"));
        this.textFileRepository = textFileRepository;
    }

    public void process(String line) {
        //https://papaprimerizo.es/java-expresion-regular-para-validar-acentos-y-n/
        line = line
                .replaceAll("[^a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ ]", " ")
                .replaceAll(" ","><")
                .replaceAll("<>","")
                .replaceAll("><"," ")
                .toLowerCase()
                .trim();

        String[] split = line.split(" ");
        List<String> terms = Arrays.asList(split);
        TreeSet<String> words = new TreeSet<>(terms);

        List<SearchAnalyzer> threads = new ArrayList<>();
        for(String key : textFileRepository.get().keySet()) {
            threads.add(new SearchAnalyzer(key,textFileRepository.get().get(key),words));
        }

        // creates a thread pool with threadCount no. of
        // threads as the fixed pool size(Step 2)
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);

        // passes the Task objects to the pool to execute (Step 3)
        threads.forEach(pool::execute);

        //https://www.baeldung.com/java-executor-service-tutorial
        // pool shutdown ( Step 4)
        pool.shutdown();
        try {
            //https://stackoverflow.com/questions/1250643/how-to-wait-for-all-threads-to-finish-using-executorservice
            if(pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS))
                System.out.println("Query completed.");
        } catch (InterruptedException e) {
            System.out.println("Shutdown");
        }

        // Sorting SearchAnalyzer entries by score
        threads.sort(new SortByScore());
        for(int i=0; i<Math.min(resultCount,threads.size()); i++) {
            SearchAnalyzer searchAnalyzer = threads.get(i);
            System.out.println(searchAnalyzer.getFileName() + ": " + searchAnalyzer.getScore() + "%");
        }
    }
}
