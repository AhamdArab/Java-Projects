package de.uniwue.jpp.javalgebra.mengen;

import de.uniwue.jpp.javalgebra.Menge;
import de.uniwue.jpp.javalgebra.Tupel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TupelMenge<T> implements Menge<Tupel<T>> {
    private final Menge<T> menge;

    public TupelMenge(Menge<T> menge) {
        if (menge.getSize().isEmpty())
            throw new IllegalArgumentException("");
        this.menge = menge;

    }

    @Override
    public Stream<Tupel<T>> getElements() {
        List<T> elements = menge.getElements().toList();
        List<Tupel<T>> list = new ArrayList<>();
        for (T first : elements) {
            for (T second : elements) {
                list.add(new Tupel<>(first, second));
            }
        }
        return list.stream();
    }

    @Override
    public boolean contains(Tupel<T> element) {
        return getElements().toList().contains(element);

    }

    @Override
    public Optional<Integer> getSize() {
        return Optional.of(getElements().toList().size());
    }
}
