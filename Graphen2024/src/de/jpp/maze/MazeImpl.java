package de.jpp.maze;

import de.jpp.io.interfaces.GraphReader;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

public class MazeImpl implements Maze {
    int width;
    int height;
    boolean[][] maze;

    public MazeImpl(int width, int height) {
        this.width = width;
        this.height = height;
        this.maze = new boolean[height * 2 - 1][width * 2 - 1];
    }

    public MazeImpl(Random random, int width, int height) {
        this(width, height);
        MazeGenerator generator = new MazeGenerator(width, height);
        this.maze = generator.generateMaze(random);
    }

    public MazeImpl() {
        super();
    }

    public GraphReader<XYNode, Double, TwoDimGraph, Maze> getMazeReader(){
        return null;
    }

    public BufferedImage getMazeAsImage(Maze input) {
        int height = input.getHeight() * 2 + 1;
        int width = input.getWidth() * 2 + 1;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        fillBackground(bufferedImage, width, height);

        drawOuterWalls(bufferedImage, width, height);

        drawInnerWalls(input, bufferedImage);

        return bufferedImage;
    }

    private void fillBackground(BufferedImage image, int width, int height) {
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.BLACK);

        IntStream.iterate(2, i -> i < height, i -> i + 4).forEach(y ->
                IntStream.iterate(2, j -> j < width, j -> j + 4).forEach(x ->
                        g2d.fillRect(x, y, 1, 1)));
    }

    private void drawOuterWalls(BufferedImage image, int width, int height) {
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.BLACK);

        g2d.drawLine(0, 0, width - 1, 0);
        g2d.drawLine(0, height - 1, width - 1, height - 1);

        g2d.drawLine(0, 0, 0, height - 1);
        g2d.drawLine(width - 1, 0, width - 1, height - 1);
        g2d.dispose();
    }

    private void drawInnerWalls(Maze input, BufferedImage image) {
        Graphics2D g2d = (Graphics2D) image.getGraphics();

        g2d.setColor(Color.BLACK);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        IntStream.range(0, input.getHeight() - 1).forEach(y ->
                IntStream.range(0, input.getWidth()).forEach(x -> {
                    if (input.isVWallActive(x, y)){
                        int adjustedX = 2 * x + 1;
                        int adjustedY = 2 * y + 2;
                        g2d.drawLine(adjustedX, adjustedY, adjustedX, adjustedY + 1);
                    }}));

        IntStream.range(0, input.getHeight()).forEach(y ->
                IntStream.range(0, input.getWidth() - 1).forEach(x -> {
                    if (input.isHWallActive(x, y))
                        image.setRGB(2 * x + 2, 2 * y + 1, Color.BLACK.getRGB());
                }));
    }

    @Override
    public void setHWall(int x, int y, boolean wallActive) {
        if (2 * y < maze.length && 2 * x + 1 < maze[0].length)
            maze[2 * y][2 * x + 1] = wallActive;
    }

    @Override
    public void setVWall(int x, int y, boolean wallActive) {
        if (2 * y + 1 < maze.length && 2 * x < maze[0].length)
            maze[2 * y + 1][2 * x] = wallActive;
    }

    @Override
    public void setAllWalls(boolean wallActive) {
        IntStream.range(0, height).forEach(y -> {
            IntStream.range(0, width).forEach(x -> {
                setVWall(x, y, wallActive);
                if (x < width - 1) {
                    setHWall(x, y, wallActive);
                }
            });
        });
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean isHWallActive(int x, int y) {
        return maze[2 * y][2 * x + 1];
    }

    @Override
    public boolean isVWallActive(int x, int y) {
        return maze[2 * y + 1][2 * x];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MazeImpl mazeImpl)) return false;
        return width == mazeImpl.width &&
                height == mazeImpl.height &&
                Arrays.deepEquals(maze, mazeImpl.maze);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }


}

