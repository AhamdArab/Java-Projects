package de.uniwue.jpp.javalgebra;

import de.uniwue.jpp.javalgebra.mengen.TupelMenge;

import java.util.List;

public class StrukturMitEinerVerknuepfung<T> {
    private Menge<T> menge;
    private Abbildung<Tupel<T>, T> verknuepfung;
    T neutralesElement;
    public StrukturMitEinerVerknuepfung(Menge<T> menge, Abbildung<Tupel<T>, T> verknuepfung) {
        if (menge.getSize().isEmpty())
            throw new IllegalArgumentException("");
        List<T> listT = menge.getElements().toList();
        for (T a : listT) {
            for (T b : listT) {
                if (!menge.contains(verknuepfung.apply(new Tupel<>(a, b))))
                    throw new IllegalArgumentException("");
            }
        }
        this.menge = menge;
        this.verknuepfung = verknuepfung;
    }

    public T apply(T t1, T t2) {
        if (!menge.contains(t1) || !menge.contains(t2))
            throw new IllegalArgumentException("");
        return verknuepfung.apply(new Tupel<>(t1, t2));
    }

    public boolean isHalbgruppe() {
        List<T> listT = menge.getElements().toList();
        for (T a : listT) {
            for (T b : listT) {
                for (T c : listT) {
                    T first = apply(apply(a, b), c);
                    T second = apply(a, apply(b, c));
                    if (!first.equals(second))
                        return false;
                }
            }
        }
        return true;
    }

    public boolean isMonoid() {
        if (!isHalbgruppe())
            return false;
        List<T> listT = menge.getElements().toList();
        for (T a : listT) {
            boolean aIsMonoid = true;
            for (T b : listT) {
                T first = apply(a, b);
                T second = apply(b, a);
                //   System.out.println(a + ", " + b + " -> " + first);
                if (!first.equals(b) || !second.equals(b)) {
                    aIsMonoid = false;
                    break;
                }
            }
            if (aIsMonoid) {
                neutralesElement = a;
                return true;
            }
        }
        return false;
    }

    public T getNeutralesElement() {
        if (!isMonoid())
            throw new UnsupportedOperationException("");
        return neutralesElement;
    }

    public boolean isGruppe() {
        if (!isMonoid())
            return false;
        List<T> listT = menge.getElements().toList();
        int c = 0;
        for (T a : listT) {
            for (T b : listT) {
                T first = apply(a, b);
                T second = apply(b, a);
                if (first.equals(neutralesElement) && second.equals(neutralesElement)) {
                    c++;
                    break;
                }

            }
        }
        return c == listT.size();
    }
    public boolean isKommutativ() {
        List<T> listT = menge.getElements().toList();
        for (T a : listT) {
            for (T b : listT) {
                T first = apply(a, b);
                T second = apply(b, a);
                if (!first.equals(second))
                    return false;
            }
        }
        return true;
    }

    public boolean isAbelscheGruppe() {
        return isKommutativ() && isGruppe();

    }
}
