package de.jpp.io.interfaces;

import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import org.jdom2.Element;

import java.util.Optional;

public class TwoDimGraphGxlReader extends GxlReaderTemplate<XYNode, Double, TwoDimGraph, String> {
    @Override
    public TwoDimGraph createGraph() {
        return new TwoDimGraph();
    }

    @Override
    public XYNode readNode(Element element) throws ParseException {
        String description = findChildByName(element, "description")
                .map(e -> e.getChildren().get(0).getText())
                .orElse(null);
        Double x = getCoordinate(element, "x");
        Double y = getCoordinate(element, "y");

        if (x == null || y == null) {
            throw new ParseException();
        }

        return new XYNode(description, x, y);
    }


    private Double getCoordinate(Element element, String coordinateName) throws ParseException {
        return findChildByName(element, coordinateName)
                .map(e -> {
                    try {
                        return Double.parseDouble(e.getChildren().get(0).getText());
                    } catch (NumberFormatException ex) {
                        throw new RuntimeException();
                    }
                })
                .orElseThrow(ParseException::new);
    }

    private Optional<Element> findChildByName(Element element, String name) {
        return element.getChildren().stream()
                .filter(child -> name.equals(child.getAttributeValue("name")))
                .findFirst();

    }


    @Override
    public Optional<Double> readAnnotation(Element element) {
        return element.getChildren("attr").stream()
                .filter(attr -> "cost".equals(attr.getAttributeValue("name")))
                .findFirst()
                .map(attr -> attr.getChildText("float", attr.getNamespace()))
                .map(Double::parseDouble);
    }
}
