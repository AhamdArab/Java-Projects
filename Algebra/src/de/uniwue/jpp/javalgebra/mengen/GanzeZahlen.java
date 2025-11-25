package de.uniwue.jpp.javalgebra.mengen;

import de.uniwue.jpp.javalgebra.Menge;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class GanzeZahlen implements Menge<BigInteger> {

    @Override
    public Stream<BigInteger> getElements() {
        return Stream.iterate(BigInteger.ZERO,bigInteger -> {
            if (bigInteger.compareTo(BigInteger.ZERO) <= 0)
                return bigInteger.negate().add(BigInteger.ONE);

            return bigInteger.negate();
        });}

    @Override
    public boolean contains(BigInteger element) {
        return true;
    }

    @Override
    public Optional<Integer> getSize() {
        return Optional.empty();
    }
}
