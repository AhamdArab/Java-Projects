package de.jpp.algorithm.interfaces;

import de.jpp.model.interfaces.Edge;

import java.util.*;
import java.util.function.BiConsumer;

public class SearchResultImpl<N, A> implements ObservableSearchResult<N, A> {
    Map<N, NodeStatus> status = new HashMap<>();
    Map<N, NodeInformation<N, A>> info = new HashMap<>();
    List<BiConsumer<N, SearchResult<N, A>>> addClose = new ArrayList<>();
    List<BiConsumer<N, SearchResult<N, A>>> addOpen = new ArrayList<>();

    @Override
    public void addNodeOpenedListener(BiConsumer<N, SearchResult<N, A>> onOpen) {
        addOpen.add(onOpen);
    }

    @Override
    public void removeNodeOpenedListener(BiConsumer<N, SearchResult<N, A>> onOpen) {
        addOpen.remove(onOpen);
    }

    @Override
    public void addNodeClosedListener(BiConsumer<N, SearchResult<N, A>> onClose) {
        addClose.add(onClose);
    }

    @Override
    public void removeNodeClosedListener(BiConsumer<N, SearchResult<N, A>> onClose) {
        addClose.remove(onClose);
    }

    @Override
    public NodeStatus getNodeStatus(N node) {
        return status.getOrDefault(node, NodeStatus.UNKOWN);
    }

    public void setNodeStatus(N node, NodeStatus nodeStatus){
        status.put(node,nodeStatus);
    }
    @Override
    public Optional<Edge<N, A>> getPredecessor(N node) {
        return Optional.ofNullable(info.get(node)).map(NodeInformation::predecessor);
    }

    @Override
    public Collection<N> getAllKnownNodes() {
        return new HashSet<>(status.keySet());
    }

    @Override
    public Collection<N> getAllOpenNodes() {
        return status.entrySet().stream()
                .filter(entry -> entry.getValue() == NodeStatus.OPEN)
                .map(Map.Entry::getKey)
                .toList();
    }


    @Override
    public void setClosed(N node) {
        status.put(node, NodeStatus.CLOSED);
        addClose.forEach(nSearchResultBiConsumer -> nSearchResultBiConsumer.accept(node, this));
    }

    @Override
    public void setOpen(N node) {
        status.put(node, NodeStatus.OPEN);
        addOpen.forEach(nSearchResultBiConsumer -> nSearchResultBiConsumer.accept(node, this));
    }

    @Override
    public void clear() {
        status.clear();
        info.clear();
    }

    @Override
    public Optional<List<Edge<N, A>>> getPathTo(N dest) {
        Deque<Edge<N, A>> stack = new ArrayDeque<>();

        while (true) {
            NodeInformation<N, A> info = getInformation(dest);
            if (info == null || info.predecessor() == null) {
                break;
            }
            stack.push(info.predecessor());
            dest = info.predecessor().getStart();
        }

        if (stack.isEmpty()) {
            return Optional.empty();
        }

        List<Edge<N, A>> path = new ArrayList<>();
        while (!stack.isEmpty()) {
            path.add(stack.pop());
        }

        return Optional.of(path);
    }

    public void open(N node, NodeInformation<N, A> information) {
        setOpen(node);
        info.put(node, information);
    }

    public NodeInformation<N, A> getInformation(N node) {
        return info.get(node);
    }

    public void setInformation(N node, NodeInformation<N, A> nodeInformation) {
        info.put(node, nodeInformation);
    }


}
