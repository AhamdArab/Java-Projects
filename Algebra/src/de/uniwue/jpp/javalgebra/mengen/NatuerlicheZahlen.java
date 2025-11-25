package de.uniwue.jpp.javalgebra.mengen;

import de.uniwue.jpp.javalgebra.Menge;

import java.math.BigInteger;
import java.util.Optional;
import java.util.stream.Stream;

public class NatuerlicheZahlen implements Menge<BigInteger> {

    @Override
    public Stream<BigInteger> getElements() {
        return Stream.iterate(BigInteger.ONE, bigInteger -> bigInteger.add(BigInteger.ONE));
    }

    @Override
    public boolean contains(BigInteger element) {
        return element.compareTo(BigInteger.ONE)>=0;
    }

    @Override
    public Optional<Integer> getSize() {
        return Optional.empty();
    }
}
