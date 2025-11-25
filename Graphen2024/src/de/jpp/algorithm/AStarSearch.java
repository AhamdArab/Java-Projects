package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.EstimationFunction;
import de.jpp.algorithm.interfaces.NodeInformation;
import de.jpp.algorithm.interfaces.SearchResult;
import de.jpp.algorithm.interfaces.SearchStopStrategy;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.WeightedGraph;

import java.util.*;

public class AStarSearch<N> extends BreadthFirstSearchTemplate<N, Double, WeightedGraph<N, Double>> {

    N destination;
    EstimationFunction<N> estToDest;
    Map<N, Double> gScores;
    PriorityQueue<N> openSet;

    public AStarSearch(WeightedGraph<N, Double> graph, N start, N destination, EstimationFunction<N> estToDest) {
        super(graph, start);
        this.destination = destination;
        this.estToDest = estToDest;
        gScores = new HashMap<>();
        gScores.put(start, 0.0);

        Comparator<N> fScoreComparator = Comparator.comparingDouble(
                n -> gScores.getOrDefault(n, Double.MAX_VALUE) + estToDest.getEstimatedDistance(n, destination)
        );
        openSet = new PriorityQueue<>(fScoreComparator);
        openSet.add(start);
    }

    @Override
    public SearchResult<N, Double> findPaths(SearchStopStrategy<N> stopStrategy) {
        while (!openSet.isEmpty() && !stopped) {
            N current = openSet.poll();
            result.setClosed(current);

            if (current.equals(destination) || stopStrategy.stopSearch(current)) {
                break;
            }
            for (Edge<N, Double> edge : graph.getNeighbours(current)) {
                N destination = edge.getDestination();
                Optional<Object> annotation = Optional.ofNullable(edge.getAnnotation());
                double edgeWeight = annotation.map(value -> 0.0).orElse(0.0);

                double tentativeGScore = gScores.getOrDefault(current, Double.MAX_VALUE) + edgeWeight;

                if (tentativeGScore < gScores.getOrDefault(destination, Double.MAX_VALUE)) {
                    result.setInformation(destination, new NodeInformation<>(edge, tentativeGScore));
                    gScores.put(destination, tentativeGScore);

                    if (!openSet.contains(destination)) {
                        openSet.add(destination);
                    }
                }
            }

        }
        return result;
    }

}
