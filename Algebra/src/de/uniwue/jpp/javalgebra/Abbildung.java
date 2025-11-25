package de.uniwue.jpp.javalgebra;

import de.uniwue.jpp.javalgebra.mengen.EndlicheMenge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Abbildung<T,S> {

    private Menge<T> definitionsmenge;
    private Menge<S> zielmenge;
    private Function<T, S> abbVorschrift;

    public Abbildung(Menge<T> definitionsmenge, Menge<S> zielmenge, Function<T, S> abbVorschrift) {
        if (definitionsmenge.getSize().isEmpty())
            throw new IllegalArgumentException("");
        if (zielmenge.getSize().isEmpty())
            throw new IllegalArgumentException("");
        List<T> elements = definitionsmenge.getElements().toList();
        for (T t : elements) {
            if (!zielmenge.contains(abbVorschrift.apply(t)))
                throw new IllegalArgumentException("");

        }
        this.definitionsmenge = definitionsmenge;
        this.zielmenge = zielmenge;
        this.abbVorschrift = abbVorschrift;
    }

    public S apply(T t) {
        if (!definitionsmenge.contains(t))
            throw new IllegalArgumentException("");
        return abbVorschrift.apply(t);
    }

    public Menge<S> getBildVon(Menge<T> m) {
        List<T> listM = m.getElements().toList();
        Set<S> set = new HashSet<>();
        for (T t : listM) {
            if (!definitionsmenge.contains(t))
                throw new IllegalArgumentException();
            set.add(apply(t));
        }
        return new EndlicheMenge<>(set);

    }

    public Menge<T> getUrbildVon(Menge<S> m) {
        List<S> listS = m.getElements().toList();
        List<T> listM = definitionsmenge.getElements().toList();
        Set<T> set = new HashSet<>();
        for (S s : listS) {
            if (!zielmenge.contains(s))
                throw new IllegalArgumentException();
            for (T t : listM) {
                if (apply(t).equals(s))
                    set.add(t);
            }
        }
        return new EndlicheMenge<>(set);
    }

    public boolean isInjektiv() {
        List<S> listS = new ArrayList<>(zielmenge.getElements().toList());
        List<T> listT = definitionsmenge.getElements().toList();
        for (T t : listT) {
            S s = apply(t);
            if (!listS.contains(s))
                return false;
            listS.remove(s);
        }
        return true;
    }

    public boolean isSurjektiv() {
        List<S> listS = new ArrayList<>(zielmenge.getElements().toList());
        List<T> listT = definitionsmenge.getElements().toList();
        for (T t : listT) {
            S s = apply(t);
            listS.remove(s);
        }
        return listS.isEmpty();
    }

    public boolean isBijektiv() {
        return isInjektiv() && isSurjektiv();
    }

    public Abbildung<S, T> getUmkehrabbildung() {
        if (!isBijektiv())
            throw new UnsupportedOperationException("");
        List<T> listM = definitionsmenge.getElements().toList();
        Function<S, T> invFunction = s -> {
            for (T t : listM)
                if (abbVorschrift.apply(t).equals(s))
                    return t;
            throw new UnsupportedOperationException("");
        };

        return new Abbildung<>(zielmenge, definitionsmenge, invFunction);
    }
}
