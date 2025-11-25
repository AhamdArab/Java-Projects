package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.*;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.WeightedGraph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class DijkstraSearch<N, G extends WeightedGraph<N, Double>> extends BreadthFirstSearchTemplate<N, Double, G> {
    Map<N, NodeInformation<N, Double>> nodeInformationMap;
    PriorityQueue<N> priorityQueue;
    Map<N, Double> shortestDistances;

    public DijkstraSearch(G graph, N start) {
        super(graph, start);
        nodeInformationMap = new HashMap<>();
        shortestDistances = new HashMap<>();
        Comparator<N> comparator = Comparator.comparingDouble(node -> shortestDistances.getOrDefault(node, Double.POSITIVE_INFINITY));
        priorityQueue = new PriorityQueue<>(comparator);
    }

    @Override
    public SearchResult<N, Double> findPaths(SearchStopStrategy<N> type) {
        nodeInformationMap.put(start, new NodeInformation<>(null, 0.0));
        shortestDistances.put(start, 0.0);
        getSearchResult().setOpen(start);
        priorityQueue.add(start);

        while (!priorityQueue.isEmpty() && !stopped) {
            N currentNode = priorityQueue.poll();
            double currentDistance = shortestDistances.getOrDefault(currentNode, Double.POSITIVE_INFINITY);
            if (currentDistance == Double.POSITIVE_INFINITY) {
                break;
            }

            getSearchResult().setClosed(currentNode);
            if (stopped) break;

            for (Edge<N, Double> edge : graph.getNeighbours(currentNode)) {
                N destination = edge.getDestination();
                Double annotation = edge.getAnnotation().orElse(Double.POSITIVE_INFINITY);

                double edgeWeight;
                edgeWeight = annotation;
                double newDistance = currentDistance + edgeWeight;

                if (newDistance < shortestDistances.getOrDefault(destination, Double.POSITIVE_INFINITY)) {
                    shortestDistances.put(destination, newDistance);
                    nodeInformationMap.put(destination, new NodeInformation<>(edge, newDistance));
                    if (getSearchResult() instanceof SearchResultImpl<N, Double> resultImpl) {
                        resultImpl.open(destination, new NodeInformation<>(edge,newDistance));
                    }
                    priorityQueue.add(destination);
                }
            }
        }

        return getSearchResult();
    }

}

