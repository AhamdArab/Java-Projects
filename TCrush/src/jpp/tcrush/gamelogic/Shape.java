package jpp.tcrush.gamelogic;

import jpp.tcrush.gamelogic.utils.Coordinate2D;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class Shape {

    /*boolean moved;
        do {
            moved = false;
            for (int col = 0; col < grid.getColumnCount(); col++) {
                for (int row = grid.getRowCount() - 1; row > 0; row--) {
                    Node belowNode = getNodeByRowColumnIndex(row, col, grid);
                    Node aboveNode = getNodeByRowColumnIndex(row - 1, col, grid);

                    if (belowNode instanceof StackPane belowStack && aboveNode instanceof StackPane aboveStack) {
                        Circle belowCircle = null;
                        Circle aboveCircle = null;

                        for (Node node : belowStack.getChildren()) {
                            if (node instanceof Circle) belowCircle = (Circle) node;
                        }
                        for (Node node : aboveStack.getChildren()) {
                            if (node instanceof Circle) aboveCircle = (Circle) node;
                        }

                        boolean isBelowEmptyOrPlus = belowCircle == null || belowCircle.getFill().equals(Color.TRANSPARENT);
                        if (isBelowEmptyOrPlus && aboveCircle != null && !aboveCircle.getFill().equals(Color.TRANSPARENT)) {
                            Color temp = (Color) aboveCircle.getFill();
                            aboveCircle.setFill(Color.TRANSPARENT);
                            if (belowCircle != null) {
                                belowCircle.setFill(temp);
                            } else {
                                Circle newCircle = new Circle();
                                newCircle.setFill(temp);
                                newCircle.radiusProperty().bind(Bindings.min(belowStack.widthProperty(), belowStack.heightProperty()).divide(2.1));
                                belowStack.getChildren().add(newCircle);
                            }
                            moved = true;
                        }
                    }
                }
            }
        } while (moved);

        boolean moved;
        do {
            moved = false;
            for (int col = 0; col < grid.getColumnCount(); col++) {
                for (int row = grid.getRowCount() - 1; row > 0; row--) {
                    Node belowNode = getNodeByRowColumnIndex(row, col, grid);
                    Node aboveNode = getNodeByRowColumnIndex(row - 1, col, grid);

                    if (belowNode instanceof StackPane belowStack && aboveNode instanceof StackPane aboveStack) {
                        Circle belowCircle = null;
                        Circle aboveCircle = null;

                        for (Node node : belowStack.getChildren()) {
                            if (node instanceof Circle) belowCircle = (Circle) node;
                        }

                        for (Node node : aboveStack.getChildren()) {
                            if (node instanceof Circle) aboveCircle = (Circle) node;
                        }

                        if (belowCircle != null && aboveCircle != null && belowCircle.getFill().equals(Color.TRANSPARENT) && !aboveCircle.getFill().equals(Color.TRANSPARENT)) {
                            Color temp = (Color) aboveCircle.getFill();
                            aboveCircle.setFill(Color.TRANSPARENT);
                            belowCircle.setFill(temp);
                            moved = true;
                        }
                    }
                }
            }
        } while (moved);
        */


    List<Coordinate2D> points;
    String name;
    int amount;
    public Shape(List<Coordinate2D> points, String name, int amount) {
        if (points == null || points.isEmpty() || amount <= 0 || name == null) {
            throw new IllegalArgumentException();
        }
        this.points = Collections.unmodifiableList(points);
        this.name = name;
        this.amount = amount;
    }

    public Shape(List<Coordinate2D> points, int amount) {
        if (points == null || points.isEmpty() || amount <= 0) {
            throw new IllegalArgumentException();
        }
        this.points = Collections.unmodifiableList(points);
        this.name = "";
        this.amount = amount;
    }


    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Coordinate2D> getPoints() {
        return points;
    }

    public int getAmount() {
        return amount;
    }

    public boolean reduceAmount() {
        if (amount > 1) {
            amount--;
            return true;
        } else {
            amount = 0;
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shape shape)) return false;
        if (this.points.size() != shape.points.size()) return false;

        return new HashSet<>(this.points).containsAll(shape.points) && new HashSet<>(shape.points).containsAll(this.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }

    @Override
    public String toString() {
        if (!name.isEmpty()) {
            return amount + ":" + name;
        } else {
            StringBuilder sb = new StringBuilder(amount + ":");
            for (Coordinate2D point : points) {
                sb.append("(").append(point.getX()).append(",").append(point.getY()).append(");");
            }
            return sb.toString();
        }
    }


    public static Shape getPointShape(int amount) {
        return new Shape(List.of(
                new Coordinate2D(0, 0)), "point", amount);
    }

    public static Shape getsRowShape(int amount) {
        return new Shape(List.of(
                new Coordinate2D(0, 0),
                new Coordinate2D(-1,0)), "sRow", amount);
    }

    public static Shape getsColumnShape(int amount) {
        return new Shape(List.of(
                new Coordinate2D(0, 0),
                new Coordinate2D(0, 1)), "sColumn", amount);
    }

    public static Shape getRowShape(int amount) {
        return new Shape(List.of(
                new Coordinate2D(0, 0),
                new Coordinate2D(-1, 0),
                new Coordinate2D(1, 0)), "row", amount);
    }

    public static Shape getColumnShape(int amount) {
        return new Shape(List.of(
                new Coordinate2D(0, 0),
                new Coordinate2D(0, 1),
                new Coordinate2D(0, 2)), "column", amount);
    }


    public static Shape getDownTShape(int amount) {
        return new Shape(List.of(
                new Coordinate2D(0, 0),
                new Coordinate2D(0, 1),
                new Coordinate2D(-1,1),
                new Coordinate2D(1,1)), "downT", amount);
    }


    public static Shape getUpTShape(int amount) {
        return new Shape(List.of(
                new Coordinate2D(0, 0),
                new Coordinate2D(-1, 0),
                new Coordinate2D(1, 0),
                new Coordinate2D(0, 1)), "upT", amount);
    }


    public static Shape getRightTShape(int amount) {
        return new Shape(List.of(
                new Coordinate2D(0, 0),
                new Coordinate2D(0, 1),
                new Coordinate2D(0,2),
                new Coordinate2D(1,1)
        ), "rightT", amount);
    }

    public static Shape getLeftTShape(int amount) {
        return new Shape(List.of(
                new Coordinate2D(0, 0),
                new Coordinate2D(0, 1),
                new Coordinate2D(0,2),
                new Coordinate2D(-1,1)
        ), "leftT", amount);
    }

}
