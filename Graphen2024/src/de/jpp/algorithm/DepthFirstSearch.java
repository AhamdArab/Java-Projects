package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.*;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class DepthFirstSearch<N, A, G extends Graph<N, A>> implements SearchAlgorithm<N, A, G> {
    G graph;
    N start;
    SearchResultImpl<N, A> result = new SearchResultImpl<>();
    boolean stopped = false;
    Set<N> visited = new HashSet<>();
    Deque<N> stack;

    public DepthFirstSearch(G graph, N start) {
        if (graph == null || start == null) {
            throw new IllegalArgumentException();
        }
        this.graph = graph;
        this.start = start;
        this.stack = new ArrayDeque<>();

    }
    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {
        stack.push(start);
        result.setOpen(start);
        while (!stack.isEmpty() && !stopped) {
            N current = stack.pop();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            result.setClosed(current);
            if (type.stopSearch(current)) {
                stopped = true;
                break;
            }

            for (Edge<N, A> edge : graph.getNeighbours(current)) {
                N neighbor = edge.getDestination();
                if (!visited.contains(neighbor)) {
                    if (!stopped) {
                        stack.push(neighbor);
                        result.setOpen(neighbor);
                    }
                    result.setInformation(neighbor, new NodeInformation<>(edge, 0));
                }
            }
        }
        return result;
    }

    @Override
    public SearchResult<N, A> findAllPaths() {
        return findPaths(lastClosedNode -> false);
    }

    @Override
    public ObservableSearchResult<N, A> getSearchResult() {
        return result;
    }

    @Override
    public N getStart() {
        return start;
    }

    @Override
    public G getGraph() {
        return graph;
    }

    @Override
    public void stop() {
        stopped = true;
        while (!stack.isEmpty()) {
            N node = stack.pop();
            if (!visited.contains(node)) {
                result.setClosed(node);
            }
        }
    }

}

