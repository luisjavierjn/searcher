package com.paris.searcher.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TextFileScheduler implements Handler<List<String>, ConcurrentHashMap<String, TreeSet<String>>>{
    private final int threadCount;

    public TextFileScheduler(Properties myProps) {
        this.threadCount = Integer.parseInt(myProps.getProperty("threadCount"));
    }

    @Override
    public ConcurrentHashMap<String,TreeSet<String>> process(List<String> listStr) {
        ConcurrentHashMap<String,TreeSet<String>> repository = new ConcurrentHashMap<>();

        if(listStr.size() == 0) {
            System.out.println("Fatal error: list of files is empty !");
            return repository;
        }

        List<Runnable> threads = new ArrayList<>();
        listStr.forEach(f -> {
            threads.add(new TextFileAnalyzer(f,repository));
        });

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
                System.out.println(threads.size() + " files loaded.");
        } catch (InterruptedException e) {
            System.out.println("Shutdown");
        }

        return repository;
    }
}
