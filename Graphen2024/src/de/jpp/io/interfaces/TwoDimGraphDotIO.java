package de.jpp.io.interfaces;

import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwoDimGraphDotIO implements GraphReader<XYNode, Double, TwoDimGraph, String>, GraphWriter<XYNode, Double, TwoDimGraph, String> {

    Pattern combinedPattern = Pattern.compile("(\\d+)\\s*->\\s*(\\d+)\\s*\\[\\s*dist\\s*=\\s*(\\d+\\.?\\d*)\\s*\\]");
    Pattern matcher = Pattern.compile("(\\d+)|x\\s*=\\s*(\\d+\\.?\\d*)|y\\s*=\\s*(\\d+\\.?\\d*)|label\\s*=\\s*((\\w+)|(\"[^\"]*\"))");

    public TwoDimGraph read(String input) throws ParseException {
        TwoDimGraph twoDimGraph = new TwoDimGraph();
        Map<Integer, XYNode> map = new HashMap<>();
        String[] lines = input.split("\n");

        for (int i = 1; i < lines.length - 1; i++) {
            String inputLine = lines[i].trim();
            if (inputLine.isBlank()) continue;
            parseLine(twoDimGraph, map, inputLine);
        }
        return twoDimGraph;
    }

    private void parseLine(TwoDimGraph twoDimGraph, Map<Integer, XYNode> map, String line) throws ParseException {
        if (line.contains("dist")) {
            parseEdge(twoDimGraph, map, line);
        } else {
            parseNode(twoDimGraph, map, line);
        }
    }

    private void parseNode(TwoDimGraph twoDimGraph, Map<Integer, XYNode> map, String line) throws ParseException {
        Map<String, String> attributes = parseAttributes(line);
        if (!attributes.containsKey("x") || !attributes.containsKey("y"))
            throw new ParseException();

        double x = Double.parseDouble(attributes.get("x"));
        double y = Double.parseDouble(attributes.get("y"));
        String label = attributes.getOrDefault("label", "");

        XYNode node = new XYNode(label, x, y);
        map.put(Integer.parseInt(attributes.get("id")), node);
        twoDimGraph.addNode(node);
    }

    private void parseEdge(TwoDimGraph twoDimGraph, Map<Integer, XYNode> map, String line) throws ParseException {
        Matcher matcher = combinedPattern.matcher(line);

        if (!matcher.find())
            throw new ParseException();

        Integer sourceId = Integer.valueOf(matcher.group(1));
        Integer destinationId = Integer.valueOf(matcher.group(2));
        Double distance = Double.valueOf(matcher.group(3));

        XYNode sourceNode = map.get(sourceId);
        XYNode destinationNode = map.get(destinationId);
        if (sourceNode == null || destinationNode == null)
            throw new ParseException();

        twoDimGraph.addEdge(sourceNode, destinationNode, Optional.of(distance));
    }

    private Map<String, String> parseAttributes(String line) {
        Map<String, String> attributes = new HashMap<>();
        Matcher match = matcher.matcher(line);
        while (match.find()) {
            if (match.group(1) != null) {
                attributes.put("id", match.group(1));
            } else if (match.group(2) != null) {
                attributes.put("x", match.group(2));
            } else if (match.group(3) != null) {
                attributes.put("y", match.group(3));
            } else if (match.group(4) != null) {
                String label = match.group(4);
                if (label.startsWith("\"") && label.endsWith("\"")) {
                    label = label.substring(1, label.length() - 1);
                }
                attributes.put("label", label);
            }
        }
        return attributes;
    }
    @Override
    public String write(TwoDimGraph graph) {

        StringBuilder sb = new StringBuilder("digraph {\n");
        Map<XYNode, Integer> nodeIds = new HashMap<>();
        AtomicInteger nodeIdCounter = new AtomicInteger(1);

        graph.getNodes().forEach(node -> {
            Integer id = nodeIdCounter.getAndIncrement();
            nodeIds.put(node, id);
            sb.append(String.format("\t%d [x=%.2f, y=%.2f, label=\"%s\"];\n", id, node.getX(), node.getY(), node.getLabel()));
        });

        graph.getEdges().forEach(edge -> {
            Integer sourceId = nodeIds.get(edge.getStart());
            Integer destinationId = nodeIds.get(edge.getDestination());
            edge.getAnnotation().ifPresent(dist -> sb.append(String.format("\t%d -> %d [dist=%.2f];\n", sourceId, destinationId, dist)));
        });

        sb.append("}");
        return sb.toString();

    }

}
