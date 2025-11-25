package gui.control;


import de.jpp.algorithm.StartToDestStrategy;
import de.jpp.algorithm.interfaces.*;
import de.jpp.factory.GraphFactory;
import de.jpp.factory.IOFactory;
import de.jpp.factory.SearchFactory;
import de.jpp.io.interfaces.GraphReader;
import de.jpp.io.interfaces.GraphWriter;
import de.jpp.io.interfaces.ParseException;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;
import gui.GraphicConfigs;
import gui.graphViews.GraphViews;
import javafx.application.Platform;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class GraphController {

    public static void addEdge(TwoDimGraph graph, Consumer<String> handleErrorMsg, XYNode start, XYNode dest, String distLabel) {
        try {
            if (start == null) {
                handleErrorMsg.accept("No start chosen");
                return;
            }
            if (dest == null) {
                handleErrorMsg.accept("No destination chosen");
                return;
            }
            if (start == dest || start.equals(dest)) {
                handleErrorMsg.accept("Sorry, loops are to hard to draw!");
            }

            try {
                graph.addEdge(start, dest, Optional.of(Double.parseDouble(distLabel)));
            } catch (NumberFormatException ex) {
                graph.addEuclidianEdge(start, dest);
            }

        } catch (Exception ex) {
            handleErrorMsg.accept("Unexpected Exception: " + ex.getMessage());
        }
    }

    public static boolean isValidPosition(TwoDimGraph graph, GraphViews graphView, Consumer<String> handleErrorMsg, XYNode node) {

        if (node.getX() < 0 || node.getX() > GraphicConfigs.getGraphSize()[0] || node.getY() < 0 || node.getY() > GraphicConfigs.getGraphSize()[1]) {
            handleErrorMsg.accept("Node at pos (" + node.getX() + "|" + node.getY() + ") would be outside of Graph Window!");
            return false;
        }

        for (XYNode xy : graph.getNodes()) {
            if (xy.euclidianDistTo(node) < graphView.getSafetyDist()) {
                handleErrorMsg.accept("Node at pos (" + node.getX() + "|" + node.getY() + ") would overlap with XYNode " + xy);
                return false;
            }
        }

        return true;
    }

    public static void addNode(TwoDimGraph graph, GraphViews graphView, Consumer<String> handleErrorMsg, String labelInput, String xInput, String yInput) {
        try {
            int x = Integer.parseInt(xInput);
            int y = Integer.parseInt(yInput);

            if (labelInput.equalsIgnoreCase("Penis")) {
                handleErrorMsg.accept("Label text too short!");
                return;
            }

            XYNode xyNode = new XYNode(labelInput, x, y);

            if (isValidPosition(graph, graphView, handleErrorMsg, xyNode))
                graph.addNode(xyNode);


            graph.addNode(new XYNode(labelInput, x, y));

        } catch (NumberFormatException ex) {
            handleErrorMsg.accept("Invalid Position: (" + xInput + "|" + yInput + ")");
        } catch (Exception ex) {
            handleErrorMsg.accept("Unexpected Exception: " + ex.getMessage());
        }

    }

    public static TwoDimGraph loadGxl(TwoDimGraph graph, GraphViews views, Consumer<String> handleErrorMsg, File file) throws ParseException, IOException {
        List<String> lines = null;

        lines = Files.readAllLines(file.toPath());


        if (lines == null || lines.isEmpty())
            handleErrorMsg.accept("File was empty");

        GraphReader<XYNode, Double, TwoDimGraph, String> in = new IOFactory().getTwoDimGxlReader();

        TwoDimGraph tdg = in.read(String.join("\n", lines));

        return tdg;
    }

    public static void loadGraph(TwoDimGraph graph, GraphViews views, Consumer<String> handleErrorMsg, File file) {


        if (file == null) {
            handleErrorMsg.accept("No graph chosen!");
            return;
        }

        new Thread(() -> {
            try {
                TwoDimGraph tdg;
                if (file.getName().endsWith("gxl"))
                    tdg = loadGxl(graph, views, handleErrorMsg, file);
                else
                    tdg = new IOFactory().getTwoDimImgReader().read(ImageIO.read(file));

                System.out.println("loaded!");

                if (tdg == null)
                    handleErrorMsg.accept("Reading failed: graph still null!");
                else
                    views.loadGraphIntoView(graph, tdg);

            } catch (Exception e) {
                e.printStackTrace();
                handleErrorMsg.accept("Error while reading file: " + e.getMessage());
            }
        }).start();


    }

    public static void storeGraph(Graph<XYNode, Double> graph, Consumer<String> handleErrorMsg, File file) {

        try {
            GraphWriter<XYNode, Double, TwoDimGraph, String> out = new IOFactory().getTwoDimGxlWriter();


            TwoDimGraph tdg = new GraphFactory().createNewTwoDimGraph();
            for (XYNode node : graph.getNodes())
                tdg.addNode(node);

            for (Edge<XYNode, Double> edge : graph.getEdges())
                tdg.addEdge(edge.getStart(), edge.getDestination(), edge.getAnnotation());

            String str = out.write(tdg);


            Files.write(file.toPath(), str.getBytes(), StandardOpenOption.CREATE);

        } catch (Exception e) {
            handleErrorMsg.accept("Exception while writing file: " + e.getMessage());
        }

    }

    public static void startSearch(TwoDimGraph graph, GraphViews graphView, Consumer<String> handleErrorMsg, Algorithm
            algorithm, XYNode start, SearchStopStrategy<XYNode> stopStrategy, GraphViews view) {

        GraphicConfigs.setSearch(null, view);

        if (algorithm == null) {
            handleErrorMsg.accept("No Algorithm selected");
            return;
        }
        if (start == null) {
            handleErrorMsg.accept("No start node selected");
            return;
        }

        if (stopStrategy == null) {
            handleErrorMsg.accept("No search stop defined!");
            return;
        }


        SearchAlgorithm<XYNode, Double, TwoDimGraph> search = getSearch(graph, algorithm, start, stopStrategy);

        if (search == null) {
            handleErrorMsg.accept("Error at retrieving search de.jpp.algorithm");
            return;
        }

        ObservableSearchResult<XYNode, Double> resultObs = search.getSearchResult();

        if (resultObs == null) {
            handleErrorMsg.accept("Error at retrieving observable search result");
        }

        resultObs.addNodeOpenedListener((node, res) -> {
            Platform.runLater(() -> {
                view.setColor(node, GraphicConfigs.paintOpen);
                if (res.getPredecessor(node).isPresent())
                    view.setColor(res.getPredecessor(node).get(), GraphicConfigs.paintEdgeOpen);
            });
            try {
                Thread.sleep(GraphicConfigs.animationWaitTime);
            } catch (Exception e) {
            }

        });

        resultObs.addNodeClosedListener((node, res) -> {
            Platform.runLater(() -> {
                        view.setColor(node, GraphicConfigs.paintClosed);
                        if (res.getPredecessor(node).isPresent())
                            view.setColor(res.getPredecessor(node).get(), GraphicConfigs.paintEdgeClosed);
                    }
            );
            try {
                Thread.sleep(GraphicConfigs.animationWaitTime);
            } catch (Exception e) {
            }

        });


        GraphicConfigs.setSearch(search, view);

        new Thread(() -> {
            SearchResult<XYNode, Double> res = search.findPaths(stopStrategy);
            try {
                StartToDestStrategy<XYNode> startToDest = (StartToDestStrategy<XYNode>) stopStrategy;
                Optional<List<Edge<XYNode, Double>>> collection = res.getPathTo(startToDest.dest());
                if (collection.isPresent())
                    view.colorPath(start, res.getPathTo(startToDest.dest()).get());

            } catch (Exception e) {
            }
        }).start();


        //SearchStopStrategy<XYNode>

    }


    private static SearchAlgorithm<XYNode, Double, TwoDimGraph> getSearch(TwoDimGraph graph, Algorithm
            algorithm, XYNode start, SearchStopStrategy<XYNode> stopStrategy) {

        SearchFactory<XYNode, Double> sFactory = new SearchFactory<>();

        if (algorithm == Algorithm.DIJKSTRA) {
            return sFactory.getDijkstra(graph, start);
        } else if (algorithm == Algorithm.ASTAR) {
            StartToDestStrategy<XYNode> stop;
            try {
                stop = (StartToDestStrategy<XYNode>) stopStrategy;
            } catch (Exception e) {
                return null;
            }
            EstimationFunction<XYNode> estToDest = (node1, node2) -> {
                double dx = node2.getX() - node1.getX();
                double dy = node2.getY() - node1.getY();
                return Math.sqrt(dx * dx + dy * dy);
            };
            return sFactory.getAStar(graph, start, stop.dest(), estToDest);
        } else if (algorithm == Algorithm.DEPTH) {
            return sFactory.getDepthFirstSearch(graph, start);
        } else {
            return sFactory.getBreadthFirstSearch(graph, start);
        }
    }

}
