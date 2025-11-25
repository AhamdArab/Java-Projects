package de.uniwue.jpp.javalgebra.mengen;

import de.uniwue.jpp.javalgebra.Menge;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class RestklassenMenge implements Menge<Integer> {

    private final Set<Integer> objects = new HashSet<>();
    public RestklassenMenge(int mod) {
        if (mod<=0)
            throw new IllegalArgumentException("");
        for (int i = 0; i < mod; i++)
            objects.add(i);

    }

    @Override
    public Stream<Integer> getElements() {
        return objects.stream();
    }

    @Override
    public boolean contains(Integer element) {
        return element >= 0 && element < objects.size();
    }

    @Override
    public Optional<Integer> getSize() {
        return Optional.of(objects.size());
    }
}
