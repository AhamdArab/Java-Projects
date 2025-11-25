package de.jpp.model.interfaces;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ObservableWeightedGraphImpl<N, A> implements ObservableGraph<N,A>{
    GraphenImpl<N, A> graph;

    List<Consumer<N>> nodeAddedListeners = new ArrayList<>();
    List<Consumer<N>> nodeRemovedListeners = new ArrayList<>();
    List<Consumer<Edge<N, A>>> edgeAddedListeners = new ArrayList<>();
    List<Consumer<Edge<N, A>>> edgeRemovedListeners = new ArrayList<>();

    List<Consumer<Collection<Edge<N, A>>>> neighboursListedListeners = new ArrayList<>();
    List<Consumer<Collection<Edge<N, A>>>> reachableListedListeners = new ArrayList<>();
    List<Consumer<Collection<N>>> nodesListedListeners = new ArrayList<>();
    List<Consumer<Collection<Edge<N, A>>>> edgesListedListeners = new ArrayList<>();

    public ObservableWeightedGraphImpl() {
        this.graph = new GraphenImpl<>();
    }


    @Override
    public boolean addNode(N node) {
        boolean added = graph.addNode(node);
        if (added) {
            nodeAddedListeners.forEach(listener -> listener.accept(node));
        }
        return added;
    }

    @Override
    public boolean addNodes(Collection<? extends N> nodes) {
        boolean isGraphChanged = false;
        for (N node : nodes) {
            isGraphChanged |= addNode(node);
        }
        return isGraphChanged;
    }

    @SafeVarargs
    @Override
    public final boolean addNodes(N... nodes) {
        return addNodes(Arrays.asList(nodes));
    }

    @Override
    public Collection<N> getNodes() {
        Collection<N> nodes = graph.getNodes();
        nodesListedListeners.forEach(listener -> listener.accept(nodes));
        return nodes;
    }

    @Override
    public Edge<N, A> addEdge(N start, N destination, Optional<A> annotation) {
        addNode(start);
        addNode(destination);

        Edge<N, A> edge = graph.addEdge(start, destination, annotation);
        if (edge != null) {
            edgeAddedListeners.forEach(listener -> listener.accept(edge));
        }

        return edge;
    }

    @Override
    public boolean removeEdge(Edge<N, A> edge) {
        boolean removed = graph.removeEdge(edge);
        if (removed) {
            edgeRemovedListeners.forEach(listener -> listener.accept(edge));
        }
        return removed;
    }

    @Override
    public Collection<Edge<N, A>> getNeighbours(N node) {
        Collection<Edge<N, A>> neighbours = graph.getNeighbours(node);
        neighboursListedListeners.forEach(listener -> listener.accept(neighbours));
        return neighbours;
    }

    @Override
    public Collection<Edge<N, A>> getReachableFrom(N node) {
        Collection<Edge<N, A>> reachable = graph.getReachableFrom(node);
        reachableListedListeners.forEach(listener -> listener.accept(reachable));
        return reachable;
    }

    @Override
    public Collection<Edge<N, A>> getEdges() {
        Collection<Edge<N, A>> edges = graph.getEdges();
        edgesListedListeners.forEach(listener -> listener.accept(edges));
        return edges;
    }

    @Override
    public boolean removeNode(N node) {
        Collection<Edge<N, A>> connectedEdges = new ArrayList<>(getEdgesConnectedToNode(node));

        for (Edge<N, A> edge : connectedEdges) {
            edgeRemovedListeners.forEach(listener -> listener.accept(edge));
            graph.removeEdge(edge);
        }
        boolean removed = graph.removeNode(node);
        if (removed) {
            nodeRemovedListeners.forEach(listener -> listener.accept(node));
        }
        return removed;
    }

    private Collection<Edge<N, A>> getEdgesConnectedToNode(N node) {
        Collection<Edge<N, A>> connectedEdges = new ArrayList<>();

        List<Edge<N, A>> outgoingEdges = graph.edges.get(node);
        if (outgoingEdges != null) {
            connectedEdges.addAll(outgoingEdges);
        }

        for (Map.Entry<N, List<Edge<N, A>>> entry : graph.edges.entrySet()) {
            for (Edge<N, A> edge : entry.getValue()) {
                if (edge.getDestination().equals(node)) {
                    connectedEdges.add(edge);
                }
            }
        }
        return connectedEdges;
    }
    @Override
    public boolean removeNodes(Collection<? extends N> nodes) {
        boolean changed = false;
        for (N node : nodes) {
            changed |= removeNode(node);
        }
        return changed;
    }

    @SafeVarargs
    @Override
    public final boolean removeNodes(N... nodes) {
        return removeNodes(Arrays.asList(nodes));
    }

    @Override
    public void clear() {
        List<N> nodesCopy = new ArrayList<>(getNodes());
        removeNodes(nodesCopy);
        graph.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObservableWeightedGraphImpl<?, ?> that)) return false;
        return Objects.equals(graph, that.graph);
    }

    @Override
    public String toString() {
        return "ObservableWeightedGraphImpl{" +
                "graph=" + graph +
                ", nodeAddedListeners=" + nodeAddedListeners +
                ", nodeRemovedListeners=" + nodeRemovedListeners +
                ", edgeAddedListeners=" + edgeAddedListeners +
                ", edgeRemovedListeners=" + edgeRemovedListeners +
                ", neighboursListedListeners=" + neighboursListedListeners +
                ", reachableListedListeners=" + reachableListedListeners +
                ", nodesListedListeners=" + nodesListedListeners +
                ", edgesListedListeners=" + edgesListedListeners +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph);
    }

    @Override
    public void addNodeAddedListener(Consumer<N> listener) {
        nodeAddedListeners.add(listener);
    }

    @Override
    public void addNodeRemovedListener(Consumer<N> listener) {
        nodeRemovedListeners.add(listener);
    }

    @Override
    public void addEdgeAddedListener(Consumer<Edge<N, A>> listener) {
        edgeAddedListeners.add(listener);
    }

    @Override
    public void addEdgeRemovedListener(Consumer<Edge<N, A>> listener) {
        edgeRemovedListeners.add(listener);
    }

    @Override
    public void removeNodeAddedListener(Consumer<N> listener) {
        nodeAddedListeners.remove(listener);
    }

    @Override
    public void removeNodeRemovedListener(Consumer<N> listener) {
        nodeRemovedListeners.remove(listener);
    }

    @Override
    public void removeEdgeAddedListener(Consumer<Edge<N, A>> listener) {
        edgeAddedListeners.remove(listener);
    }

    @Override
    public void removeEdgeRemovedListener(Consumer<Edge<N, A>> listener) {
        edgeRemovedListeners.remove(listener);
    }

    @Override
    public void addNeighboursListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        neighboursListedListeners.add(listener);
    }

    @Override
    public void addReachableListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        reachableListedListeners.add(listener);
    }

    @Override
    public void addNodesListedListener(Consumer<Collection<N>> listener) {
        nodesListedListeners.add(listener);
    }

    @Override
    public void addEdgesListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        edgesListedListeners.add(listener);
    }

    @Override
    public void removeNeighboursListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        neighboursListedListeners.remove(listener);
    }

    @Override
    public void removeReachableListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        reachableListedListeners.remove(listener);
    }

    @Override
    public void removeNodesListedListener(Consumer<Collection<N>> listener) {
        nodesListedListeners.remove(listener);
    }

    @Override
    public void removeEdgesListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        edgesListedListeners.remove(listener);
    }

}
