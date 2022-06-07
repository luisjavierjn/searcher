package com.paris.searcher.pipeline;

//https://java-design-patterns.com/patterns/pipeline/
public interface Handler<I, O> {
    O process(I input);
}
