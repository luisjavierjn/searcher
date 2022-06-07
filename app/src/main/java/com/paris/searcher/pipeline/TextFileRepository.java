package com.paris.searcher.pipeline;

import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

//https://www.geeksforgeeks.org/concurrenthashmap-in-java/
public class TextFileRepository implements Handler<ConcurrentHashMap<String,TreeSet<String>>,Integer> {
    public ConcurrentHashMap<String,TreeSet<String>> repository;

    @Override
    public Integer process(ConcurrentHashMap<String, TreeSet<String>> repository) {
        this.repository = repository;
        return repository.size();
    }

    public ConcurrentHashMap<String,TreeSet<String>> get() {
        return repository;
    }

    public int size() { return repository.size(); }
}
