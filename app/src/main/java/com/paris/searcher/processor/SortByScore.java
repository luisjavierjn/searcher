package com.paris.searcher.processor;

import java.util.Comparator;

//https://www.geeksforgeeks.org/comparator-interface-java/
//https://www.baeldung.com/java-comparator-comparable
public class SortByScore implements Comparator<SearchAnalyzer> {

    @Override
    public int compare(SearchAnalyzer o1, SearchAnalyzer o2) {
        return Integer.compare(o2.getScore(), o1.getScore());
    }
}
