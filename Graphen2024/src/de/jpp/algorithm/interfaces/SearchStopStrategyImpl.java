package de.jpp.algorithm.interfaces;

public class SearchStopStrategyImpl<N> implements SearchStopStrategy<N> {
    int count;

    public SearchStopStrategyImpl(int maxCount) {
        this.count = maxCount;
    }

    @Override
    public boolean stopSearch(N lastClosedNode) {
        if (count <= 0)
            return true;
        count--;
        return count == 0;
    }

}
