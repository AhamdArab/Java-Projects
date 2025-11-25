package de.uniwue.jpp.javalgebra;

import com.sun.source.tree.EmptyStatementTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface Menge<T> {

    Stream<T> getElements();

    default boolean contains(T element) {
        return getElements().toList().contains(element);
    }
    default Optional<Integer> getSize() {
        return  Optional.of((int)getElements().count());
    }
    default boolean isEmpty() {
        return getSize().isPresent() && getSize().get()==0;
    }
    default String asString(int maxDisplay) {
        if (maxDisplay<=0)
            throw new IllegalArgumentException("");
        List<T> elements = getElements().limit(maxDisplay).toList();
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (T element : elements)
            builder.append(element).append(", ");
        if (getSize().isEmpty() || maxDisplay < getSize().get()) {
            builder.append("...]");
            return builder.toString();
        }
        int toDel = builder.lastIndexOf(",");
        if (toDel!=-1)
            builder.delete(toDel, toDel + 3);
        builder.append("]");
        return builder.toString();
    }
    default String asString() {
        if (getSize().isEmpty())
            return asString(10);
        if (getSize().get()==0)
            return "[]";
        return asString(getSize().get());
    }
}
