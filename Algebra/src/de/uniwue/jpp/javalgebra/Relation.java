package de.uniwue.jpp.javalgebra;

import de.uniwue.jpp.javalgebra.mengen.EndlicheMenge;
import de.uniwue.jpp.javalgebra.mengen.TupelMenge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Relation<T> implements Menge<Tupel<T>> {
    private Menge<Tupel<T>> subMenge;
    private BiFunction<T, T, Boolean> isInRelation;
    private Menge<T> menge;

    public Relation(Menge<T> menge, BiFunction<T, T, Boolean> isInRelation) {
        if (menge.getSize().isEmpty())
            throw new IllegalArgumentException("");
        this.isInRelation = isInRelation;
        this.menge = menge;
        TupelMenge<T> tupelMenge = new TupelMenge<>(menge);
        Set<Tupel<T>> set = new HashSet<>();
        List<Tupel<T>> elements = tupelMenge.getElements().toList();
        for (Tupel<T> tupel : elements) {
            T a = tupel.getFirst();
            T b = tupel.getSecond();
            if (isInRelation.apply(a, b))
                set.add(tupel);
        }
        subMenge = new EndlicheMenge<>(set);
    }

    @Override
    public Stream<Tupel<T>> getElements() {
        return subMenge.getElements();
    }

    @Override
    public boolean contains(Tupel<T> element) {
        return subMenge.contains(element);
    }

    public boolean isReflexiv() {

        List<T> elements = menge.getElements().toList();
        for (T t : elements) {
            if (!contains(new Tupel<>(t, t)))
                return false;
        }
        return true;

    }

    public boolean isIrreflexiv() {
        List<T> elements = menge.getElements().toList();
        for (T t : elements) {
            if (contains(new Tupel<>(t, t)))
                return false;
        }
        return true;

    }

    public boolean isSymmetrisch() {
        List<Tupel<T>> elements = getElements().toList();
        for (Tupel<T> tupel : elements) {
            T a = tupel.getFirst();
            T b = tupel.getSecond();
            if (!contains(new Tupel<>(b, a)))
                return false;
        }
        return true;
    }

    public boolean isAsymmetrisch() {

        List<Tupel<T>> elements = getElements().toList();
        for (Tupel<T> tupel : elements) {
            T a = tupel.getFirst();
            T b = tupel.getSecond();
            if (contains(new Tupel<>(b, a)))
                return false;
        }
        return true;
    }

    public boolean isAntisymmetrisch() {
        List<Tupel<T>> elements = getElements().toList();
        for (Tupel<T> tupel : elements) {
            T a = tupel.getFirst();
            T b = tupel.getSecond();
            if (contains(new Tupel<>(b, a)) && !a.equals(b))
                return false;
        }
        return true;
    }

    public boolean isTransitiv() {
        List<Tupel<T>> elements = getElements().toList();
        for (Tupel<T> tupel1 : elements) {
            for (Tupel<T> tupel2 : elements) {
                T a = tupel1.getFirst();
                T b = tupel1.getSecond();
                T c1 = tupel2.getFirst();
                T c2 = tupel2.getSecond();
                if (contains(new Tupel<>(b, c1)) && !contains(new Tupel<>(a, c1)))
                    return false;
                if (contains(new Tupel<>(b, c2)) && !contains(new Tupel<>(a, c2)))
                    return false;
            }
        }
        return true;
    }

    public boolean isTotal() {
        List<T> elements = menge.getElements().toList();
        for (T a : elements) {
            for (T b : elements) {
                if (!contains(new Tupel<>(a, b)) && !contains(new Tupel<>(b, a)))
                    return false;
            }
        }
        return true;

    }

    public boolean isAequivalenzrelation() {
        return isReflexiv() && isSymmetrisch() && isTransitiv();
    }

    public Set<Set<T>> getAequivalenzklassen() {
        if (!isAequivalenzrelation())
            throw new UnsupportedOperationException("");
        Set<Set<T>> set = new HashSet<>();
        List<T> elements = menge.getElements().toList();
        for (T a : elements) {
            Set<T> inner = new HashSet<>();
            for (T b : elements) {
                if (contains(new Tupel<>(a, b)) && contains(new Tupel<>(b, a)))
                    inner.add(b);
            }
            set.add(inner);
        }
        return set;
    }

    public boolean isTotalordnung() {
        return isReflexiv() && isAntisymmetrisch() && isTransitiv() && isTotal();
    }

    public List<T> getElementsInOrder() {
        if (!isTotalordnung())
            throw new UnsupportedOperationException("");
        List<T> mengeElements = menge.getElements().toList();
        List<T> elements = new ArrayList<>(mengeElements);
        for (int i = 0; i < elements.size(); i++) {
            for (int j = 0; j < elements.size() - 1; j++) {
                T a = elements.get(j);
                T b = elements.get(j + 1);
                if (!isInRelation.apply(a, b)) {
                    elements.set(j, b);
                    elements.set(j + 1, a);
                }
            }
        }
        return elements;

    }
}
