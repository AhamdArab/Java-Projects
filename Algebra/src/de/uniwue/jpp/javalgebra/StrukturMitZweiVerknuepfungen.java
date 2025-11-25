package de.uniwue.jpp.javalgebra;

import de.uniwue.jpp.javalgebra.mengen.EndlicheMenge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StrukturMitZweiVerknuepfungen<T> {
    private final Menge<T> menge;
    private final Abbildung<Tupel<T>, T> plus;
    private final Abbildung<Tupel<T>, T> mal;

    public StrukturMitZweiVerknuepfungen(Menge<T> menge, Abbildung<Tupel<T>, T> plus, Abbildung<Tupel<T>, T> mal) {
        if (menge.getSize().isEmpty())
            throw new IllegalArgumentException("");
        // test if closed menge from StrukturMitEinerVerknuepfung
        StrukturMitEinerVerknuepfung<T> stPlus = new StrukturMitEinerVerknuepfung<>(menge, plus);
        StrukturMitEinerVerknuepfung<T> stMul = new StrukturMitEinerVerknuepfung<>(menge, mal);
        this.menge = menge;
        this.plus = plus;
        this.mal = mal;

    }

    public T applyPlus(T t1, T t2) {
        if (!menge.contains(t1) || !menge.contains(t2))
            throw new IllegalArgumentException("");
        return plus.apply(new Tupel<>(t1, t2));
    }

    public T applyMal(T t1, T t2) {
        if (!menge.contains(t1) || !menge.contains(t2))
            throw new IllegalArgumentException("");
        return mal.apply(new Tupel<>(t1, t2));
    }

    public boolean isDistributiv() {
        List<T> list = menge.getElements().toList();
        for (T a : list) {
            for (T b : list) {
                for (T c : list) {
                    if (!applyMal(a, applyPlus(b, c)).equals(applyPlus(applyMal(a, b), applyMal(a, c))))
                        return false;
                    if (!applyMal(applyPlus(a, b), c).equals(applyPlus(applyMal(a, c), applyMal(b, c))))
                        return false;
                }
            }
        }
        return true;
    }

    public boolean isRing() {
        StrukturMitEinerVerknuepfung<T> st1 = new StrukturMitEinerVerknuepfung<>(menge, plus);
        StrukturMitEinerVerknuepfung<T> st2 = new StrukturMitEinerVerknuepfung<>(menge, mal);
        return st1.isAbelscheGruppe() && st2.isHalbgruppe() && isDistributiv();
    }

    public T getNull() {
        if (!isRing())
            throw new UnsupportedOperationException("");
        StrukturMitEinerVerknuepfung<T> st1 = new StrukturMitEinerVerknuepfung<>(menge, plus);
        if (st1.isMonoid())
            return st1.getNeutralesElement();
        return null;
    }

    public boolean isKoerper() {
        if (!isRing())
            return false;
        Set<T> set = menge.getElements().collect(Collectors.toSet());
        set.remove(getNull());
        try {


        EndlicheMenge<T> endlicheMenge = new EndlicheMenge<>(set);
        StrukturMitEinerVerknuepfung<T> st2 = new StrukturMitEinerVerknuepfung<>(endlicheMenge, mal);
        return st2.isAbelscheGruppe();
        }
        catch (IllegalArgumentException e ){
            return false;}
    }

    public T getEins() {
        if (!isKoerper())
            throw new UnsupportedOperationException("");
        Set<T> set = menge.getElements().collect(Collectors.toSet());
        set.remove(getNull());

            EndlicheMenge<T> endlicheMenge = new EndlicheMenge<>(set);
            StrukturMitEinerVerknuepfung<T> st2 = new StrukturMitEinerVerknuepfung<>(endlicheMenge, mal);
        return st2.getNeutralesElement();
    }
}
