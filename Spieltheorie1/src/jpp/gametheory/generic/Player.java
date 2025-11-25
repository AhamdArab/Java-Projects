package jpp.gametheory.generic;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Player<C extends IChoice> implements IPlayer<C> {

    String name; IStrategy<C> strategy;

    public Player(String name, IStrategy<C> strategy) {
        this.name = Objects.requireNonNull(name);
        this.strategy = Objects.requireNonNull(strategy);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IStrategy<C> getStrategy() {
        return strategy;
    }

    @Override
    public C getChoice(List<IGameRound<C>> previousRounds) {
        Objects.requireNonNull(previousRounds);
        return strategy.getChoice(this,previousRounds);
    }

    @Override
    public int compareTo(IPlayer<C> o) {
        Comparator<IPlayer<C>> comparator = Comparator.comparing(IPlayer::getName);
        return comparator.compare(this,o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player<?> player = (Player<?>) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + "(" + strategy + ")";
    }
}
