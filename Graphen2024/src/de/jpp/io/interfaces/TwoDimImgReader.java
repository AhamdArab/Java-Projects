package de.jpp.io.interfaces;

import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TwoDimImgReader implements GraphReader<XYNode, Double, TwoDimGraph, BufferedImage> {

    @Override
    public TwoDimGraph read(BufferedImage input) {
        TwoDimGraph graph = new TwoDimGraph();
        Map<Point, XYNode> pointToNodeMap = new HashMap<>();

        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                if (isPixelBright(input, x, y)) {
                    XYNode node = new XYNode("node@" + x + "," + y, x, y);
                    graph.addNode(node);
                    pointToNodeMap.put(new Point(x, y), node);
                }
            }
        }

        pointToNodeMap.forEach((point, xyNode) -> createEdgesForAdjacentNodes(point, xyNode, pointToNodeMap, graph));

        return graph;
    }

    private void createEdgesForAdjacentNodes(Point point, XYNode currentNode, Map<Point, XYNode> map, TwoDimGraph graph) {
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] dir : directions) {
            Point neighborPoint = new Point(point.x + dir[0], point.y + dir[1]);
            XYNode neighborNode = map.get(neighborPoint);
            if (neighborNode != null) {
                double distance = Math.sqrt(Math.pow(dir[0], 2) + Math.pow(dir[1], 2));
                graph.addEdge(currentNode, neighborNode, Optional.of(distance));
            }
        }
    }

    private boolean isPixelBright(BufferedImage image, int posX, int posY) {
        int color = image.getRGB(posX, posY);
        Color colorObj = new Color(color);
        float[] floats = Color.RGBtoHSB(colorObj.getRed(), colorObj.getGreen(), colorObj.getBlue(), null);
        return floats[2] >= 0.5;
    }

}
