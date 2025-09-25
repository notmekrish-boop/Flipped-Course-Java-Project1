package edu.ccrm.domain;

import java.util.List;
import java.util.function.Predicate;

@FunctionalInterface
public interface Searchable<T> {
    List<T> search(Predicate<T> predicate);
    
    default List<T> searchAll() {
        return search(_ -> true);  // Use _ for unused parameter
    }
}