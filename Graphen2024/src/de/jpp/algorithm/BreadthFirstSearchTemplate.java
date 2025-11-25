package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.*;
import de.jpp.model.interfaces.Graph;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class BreadthFirstSearchTemplate<N, A, G extends Graph<N, A>> implements SearchAlgorithm<N, A, G> {
    G graph;
    N start;
    SearchResultImpl<N, A> result = new SearchResultImpl<>();

    boolean stopped = false;
    Deque<N> stack;
    public BreadthFirstSearchTemplate(G graph, N start) {
        if (graph == null || start == null) {
            throw new IllegalArgumentException();
        }
        this.graph = graph;
        this.start = start;
        this.stack = new ArrayDeque<>();

    }

    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {
        stack.add(start);
        result.setOpen(start);
        while (!stack.isEmpty()) {
            N current = stack.poll();
            result.setClosed(current);

            if (type.stopSearch(current)) {
                stopped = true;
                break;
            }
            if (stopped) {
                break;
            }
            graph.getNeighbours(current).forEach(edge -> {
                N neighbour = edge.getDestination();
                if (result.getNodeStatus(neighbour) == NodeStatus.UNKOWN) {
                    stack.add(neighbour);
                    result.setOpen(neighbour);
                    result.setInformation(neighbour, new NodeInformation<>(edge, 0.0));
                }
            });

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
    }

}
