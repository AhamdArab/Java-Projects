package de.jpp.io.interfaces;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class GxlWriterTemplate<N,A,G extends Graph<N,A> , String > implements GraphWriter<N,A,G, java.lang.String> {

    AtomicInteger idCounter = new AtomicInteger(1);
    Map<Object, String> idMap = new HashMap<>();

    @Override
    public java.lang.String write(G graph) {
        Document doc = new Document();
        Element gxl = new Element("gxl");
        doc.setRootElement(gxl);

        Element element = new Element("graph");
        gxl.addContent(element);
        element.setAttribute("id", "G");

        graph.getNodes().forEach(node -> {
            Element nodeElement = writeNode(node);
            nodeElement.setAttribute("id", (java.lang.String) getOrCreateId(node));
            element.addContent(nodeElement);
        });

        graph.getEdges().forEach(edge -> {
            Element edgeElement = writeEdge(edge);
            edgeElement.setAttribute("id", (java.lang.String) getOrCreateId(edge));
            element.addContent(edgeElement);
        });

        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        return outputter.outputString(doc);
    }

    private String getOrCreateId(Object obj) {
        return idMap.computeIfAbsent(obj, k -> (String) ("id" + idCounter.getAndIncrement()));
    }

    public abstract Element writeNode(N node);

    public abstract Element writeEdge(Edge<N,A> edge);

}

