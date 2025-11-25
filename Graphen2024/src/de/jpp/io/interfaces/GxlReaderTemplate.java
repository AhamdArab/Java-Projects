package de.jpp.io.interfaces;

import de.jpp.model.interfaces.Graph;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;


public abstract class GxlReaderTemplate<N, A, G extends Graph<N, A>, F> implements GraphReader<N, A, G, F> {

    private class NodeWrapper {
        String nodeId;
        N node;

        NodeWrapper(String nodeId, N node) {
            this.nodeId = nodeId;
            this.node = node;
        }
    }

    @Override
    public G read(F input) throws ParseException {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new StringReader(input.toString()));
            G graph = createGraph();

            Element graphElement = getGraphElement(document);
            List<NodeWrapper> nodeWrappers = readNodes(graph, graphElement);
            readEdges(graph, graphElement, nodeWrappers);

            return graph;
        }catch (RuntimeException | JDOMException | IOException e) {
            throw new ParseException();
        }
    }

    private Element getGraphElement(Document document) throws ParseException {
        Element graphElement = document.getRootElement().getChild("graph");
        if (graphElement == null) {
            throw new ParseException();
        }
        return graphElement;
    }

    private List<NodeWrapper> readNodes(G graph, Element graphElement) {
        List<NodeWrapper> nodeWrappers = new ArrayList<>();
        graphElement.getChildren("node").forEach(nodeElement -> {
            N node;
            try {
                node = readNode(nodeElement);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String nodeId = nodeElement.getAttributeValue("id");
            if (nodeId == null) {
                throw new RuntimeException(new ParseException());
            }
            nodeWrappers.add(new NodeWrapper(nodeId, node));
            graph.addNode(node);
        });
        return nodeWrappers;
    }

    private void readEdges(G graph, Element graphElement, List<NodeWrapper> nodeWrappers) {
        graphElement.getChildren("edge").forEach(edgeElement -> {
            String fromId = edgeElement.getAttributeValue("from");
            String toId = edgeElement.getAttributeValue("to");
            N fromNode ;
            try {
                fromNode = getNodeById(nodeWrappers, fromId);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            N toNode;
            try {
                toNode = getNodeById(nodeWrappers, toId);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Optional<A> annotation = readAnnotation(edgeElement);
            graph.addEdge(fromNode, toNode, annotation);
        });
    }

    private N getNodeById(List<NodeWrapper> nodeWrappers, String nodeId) throws ParseException {
        return nodeWrappers.stream()
                .filter(wrapper -> wrapper.nodeId.equals(nodeId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(new ParseException()))
                .node;
    }

    public abstract G createGraph();

    public abstract N readNode(Element element) throws ParseException;

    public abstract Optional<A> readAnnotation(Element element);


}

