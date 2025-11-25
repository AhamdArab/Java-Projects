package de.jpp.io.interfaces;

import de.jpp.model.LabelMapGraph;
import org.jdom2.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LabelMapGraphGxlReader extends GxlReaderTemplate<String, Map<String, String>, LabelMapGraph, String> {


    @Override
    public LabelMapGraph createGraph() {
        return new LabelMapGraph();
    }

    @Override
    public String readNode(Element nodeElement) throws ParseException {
        Optional<String> description = nodeElement.getChildren("attr").stream()
                .filter(e -> "description".equals(e.getAttributeValue("name")))
                .findFirst()
                .map(e -> e.getChildText("string"));
        String nodeName = description.orElseGet(() -> Optional.ofNullable(nodeElement.getAttributeValue("name"))
                .filter(name -> !name.isEmpty())
                .orElseGet(() -> nodeElement.getAttributeValue("id")));
        if (nodeName == null || nodeName.isEmpty()) {
            throw new ParseException();
        }

        return nodeName;
    }


    @Override
    public Optional<Map<String, String>> readAnnotation(Element element) {
        Map<String, String> annotations = element.getChildren("attr").stream()
                .collect(Collectors.toMap(
                        attr -> attr.getAttributeValue("name"),
                        attr -> {
                            Element child = attr.getChildren().size() > 0 ? attr.getChildren().get(0) : null;
                            return child != null ? child.getTextTrim() : "";
                        }
                ));
        return annotations.isEmpty() ? Optional.empty() : Optional.of(annotations);
    }
}
