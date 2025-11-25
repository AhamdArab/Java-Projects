package jpp.tcrush.gamelogic;

import jpp.tcrush.gamelogic.field.Cell;
import jpp.tcrush.gamelogic.field.GameFieldElement;
import jpp.tcrush.gamelogic.field.GameFieldElementType;
import jpp.tcrush.gamelogic.field.GameFieldItemType;
import jpp.tcrush.gamelogic.utils.Coordinate2D;
import jpp.tcrush.gamelogic.utils.Move;

import java.util.*;

public class Level {

    Map<Coordinate2D, GameFieldElement> fieldMap;
    List<Shape> allowedShapes;

    public Level(Map<Coordinate2D, GameFieldElement> fieldMap, List<Shape> allowedShapes) {
        if(Objects.isNull(fieldMap) || Objects.isNull(allowedShapes)){
            throw new IllegalArgumentException();
        }
        this.fieldMap = fieldMap;
        this.allowedShapes = allowedShapes;
        if(getHeight() < 2 || getWidth() < 2 || allowedShapes.isEmpty()){
            throw new IllegalArgumentException();
        }


    }

    public int getHeight() {
        return fieldMap.keySet().stream().mapToInt(Coordinate2D::getY).max().orElse(-1) + 1;
    }

    public int getWidth() {
        return fieldMap.keySet().stream().mapToInt(Coordinate2D::getX).max().orElse(-1) + 1;
    }

    public Map<Coordinate2D, GameFieldElement> getField() {
        return Collections.unmodifiableMap(fieldMap);
    }

    public List<Shape> getAllowedShapes() {
        return Collections.unmodifiableList(allowedShapes);
    }

    public Optional<Collection<Coordinate2D>> fit(Shape shape, Coordinate2D position) {
        if (Objects.isNull(shape) || Objects.isNull(position)) {
            throw new IllegalArgumentException();
        }

        List<Coordinate2D> absoluteCoordinates = shape.getPoints().stream()
                .map(coorShape -> new Coordinate2D(position.getX() + coorShape.getX(), position.getY() - coorShape.getY())).toList();

        GameFieldItemType expectedType = null;
        for (Coordinate2D coord : absoluteCoordinates) {
            GameFieldElement element = fieldMap.get(coord);
            if (element == null || element.getType() != GameFieldElementType.CELL || element.getItem().isEmpty()) {
                return Optional.empty();
            }
            if (expectedType == null) {
                expectedType = element.getItem().get().getType();
            } else if (element.getItem().get().getType() != expectedType) {
                return Optional.empty();
            }
        }
        return Optional.of(new HashSet<>(absoluteCoordinates));
    }

    public Collection<Move> setShapeAndUpdate(Shape shape, Coordinate2D position){
        if(Objects.isNull(shape) || Objects.isNull(position) || shape.getAmount() <= 0){
            throw new IllegalArgumentException();
        }
        try {
            List<Coordinate2D> coordinate2DList2 = new ArrayList<>();
            for(Coordinate2D coorShape: shape.getPoints()){
                coordinate2DList2.add(new Coordinate2D(position.getX() + coorShape.getX(), position.getY() - coorShape.getY()));
            }
            for (Coordinate2D coordinate2D : coordinate2DList2) {
                if (!fieldMap.get(coordinate2DList2.get(0)).getItem().get().getType().equals(fieldMap.get(coordinate2D).getItem().get().getType())) {
                    throw new IllegalArgumentException();
                }
            }
            Optional<Collection<Coordinate2D>> fit = fit(shape, position);
            List<Move> moveList = new ArrayList<>();
            if (fit.isPresent()) {
                List<GameFieldElement> listeDerPredecessors = new ArrayList<>();
                for (Coordinate2D coordinate2D : coordinate2DList2) {

                    GameFieldElement gameFieldElement = fieldMap.get(coordinate2D);
                    if (gameFieldElement.getPredecessor().isPresent()) {
                        listeDerPredecessors.add(gameFieldElement.getPredecessor().get());
                    }
                    if (gameFieldElement.getType() == GameFieldElementType.CELL) {
                        gameFieldElement.setItem(Optional.empty());
                    }
                }

                for (GameFieldElement listeDerPredecessor : listeDerPredecessors) {
                    listeDerPredecessor.update(moveList);
                }
            }
            int index = 0;
            int gibtShape = 0;

            for (int j = 0; j < allowedShapes.size(); j++) {
                try{
                    if(allowedShapes.get(j).equals(shape)){
                        index = j;
                        gibtShape += 1;
                    }
                }catch (Exception ignored){

                }
            }
            if(gibtShape == 0){
                throw new IllegalArgumentException();
            }

            for (int i = 0; i < shape.getAmount(); i++) {
                allowedShapes.get(index).reduceAmount();
                if(allowedShapes.get(index).getAmount() == 0){
                    allowedShapes.remove(index);
                }
            }
            return moveList;
        }catch (Exception e){
            throw new IllegalArgumentException();
        }
    }

    public boolean isWon() {
        return fieldMap.values().stream().allMatch(element -> {
            if (element.getType() == GameFieldElementType.BLOCK || element.getType() == GameFieldElementType.FALLTHROUGH) {
                return true;
            } else if (element instanceof Cell) {
                return element.getItem().isEmpty();
            }
            return false;
        });
    }

    public boolean canMakeAnyMove() {
        return fieldMap.values().stream()
                .anyMatch(element -> element.getSuccessor().isPresent() && element.getItem().isPresent());
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TCrush-LevelDefinition:\n");
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                GameFieldElement element = fieldMap.get(new Coordinate2D(x, y));
                sb.append(element == null ? " " : element.toString());
            }
            sb.append("\n");
        }

        sb.append("\nShapes:\n");
        for (int i = 0; i < allowedShapes.size(); i++) {
            sb.append(allowedShapes.get(i).toString());
            if (i < allowedShapes.size() - 1) {
                sb.append("\n");
            }

        }

        return sb.toString();
    }

}
