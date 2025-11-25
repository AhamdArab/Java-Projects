package de.jpp.io.interfaces;

import de.jpp.model.LabelMapGraph;
import de.jpp.model.interfaces.Edge;
import org.jdom2.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LabelMapGraphGxlWriter extends GxlWriterTemplate<String, Map<String, String>, LabelMapGraph, String> {

    AtomicInteger nextId = new AtomicInteger(1);
    Map<Object, String> idMap = new HashMap<>();

    @Override
    public Element writeNode(String node) {
        String id = getOrCreateId(node);
        Element nodeElement = new Element("node").setAttribute("id", id);
        nodeElement.addContent(createAttributeElement("description", node));
        return nodeElement;
    }

    @Override
    public Element writeEdge(Edge<String, Map<String, String>> edge) {
        String id = getOrCreateId(edge);
        Element edgeElement = new Element("edge")
                .setAttribute("id", id)
                .setAttribute("from", getOrCreateId(edge.getStart()))
                .setAttribute("to", getOrCreateId(edge.getDestination()));

        edge.getAnnotation().ifPresent(annotationMap -> {
            annotationMap.forEach((key, value) -> {
                edgeElement.addContent(createAttributeElement(key, value));
            });
        });

        return edgeElement;
    }

    private String getOrCreateId(Object obj) {
        return idMap.computeIfAbsent(obj, k -> "id" + nextId.getAndIncrement());
    }

    private Element createAttributeElement(String name, String value) {
        Element attrElement = new Element("attr").setAttribute("name", name);
        Element dataElement = new Element("string").setText(value);
        attrElement.addContent(dataElement);
        return attrElement;
    }
}
