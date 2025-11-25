package de.uniwue.jpp.javalgebra.mengen;

import de.uniwue.jpp.javalgebra.Menge;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class EndlicheMenge<T> implements Menge<T> {
    private Set<T> objects;

    public EndlicheMenge(Set<T> objects) {

        this.objects=objects;
    }

    @Override
    public Stream<T> getElements() {
        return objects.stream();
    }

    public EndlicheMenge<T> createUntermenge(Predicate<T> filter) {
        Set<T> filterSet=new HashSet<>();
        for (T t: objects) {
            if (filter.test(t))
                filterSet.add(t);
        }
        return new EndlicheMenge<>(filterSet);
    }
}
