package de.jpp.io.interfaces;

import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;
import org.jdom2.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class TwoDimGraphGxlWriter extends GxlWriterTemplate<XYNode, Double, TwoDimGraph, String> {
    Map<Object, String> idMap = new HashMap<>();
    AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Element writeNode(XYNode node) {
        Element nodeElement = new Element("node").setAttribute("id", getOrCreateId(node));
        addAttribute(nodeElement, "description", node.getLabel());
        addAttribute(nodeElement, "x", String.valueOf(node.getX()));
        addAttribute(nodeElement, "y", String.valueOf(node.getY()));
        return nodeElement;
    }

    @Override
    public Element writeEdge(Edge<XYNode, Double> edge) {
        Element edgeElement = new Element("edge")
                .setAttribute("id", getOrCreateId(edge))
                .setAttribute("from", getOrCreateId(edge.getStart()))
                .setAttribute("to", getOrCreateId(edge.getDestination()));

        edge.getAnnotation().ifPresent(annotation ->
                addAttribute(edgeElement, "cost", String.valueOf(annotation)));

        return edgeElement;
    }

    private void addAttribute(Element parent, String name, String value) {
        parent.addContent(new Element("attr").setAttribute("name", name)
                .addContent(new Element("float").setText(value)));
    }

    private String getOrCreateId(Object obj) {
        return idMap.computeIfAbsent(obj, k -> "id" + nextId.getAndIncrement());
    }

}
