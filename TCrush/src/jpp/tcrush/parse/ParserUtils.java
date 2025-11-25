package jpp.tcrush.parse;

import jpp.tcrush.gamelogic.Shape;
import jpp.tcrush.gamelogic.field.*;
import jpp.tcrush.gamelogic.utils.Coordinate2D;

import java.util.*;

public class ParserUtils {



    public static Coordinate2D parseStringToCoordinate(String s) {
        if (!s.contains("(") || !s.contains(")") || !s.contains(",") || s.length() > 10 || s.contains(".")) {
            throw new InputMismatchException(s);
        }

        try {
            s = s.replace("(", "").replace(")", "");
            String[] parts = s.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            return new Coordinate2D(x, y);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(s);
        }
    }

    public static Shape parseStringToShape(String s) {
        if (!s.contains(":")) {
            throw new InputMismatchException();
        }

        String[] parts = s.split(":", 2);
        if (parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new InputMismatchException();
        }

        int amount;
        try {
            amount = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            throw new InputMismatchException("Amount is not a valid integer: " + parts[0]);
        }

        return switch (parts[1]) {
            case "point" -> Shape.getPointShape(amount);
            case "sRow" -> Shape.getsRowShape(amount);
            case "sColumn" -> Shape.getsColumnShape(amount);
            case "row" -> Shape.getRowShape(amount);
            case "column" -> Shape.getColumnShape(amount);
            case "upT" -> Shape.getUpTShape(amount);
            case "downT" -> Shape.getDownTShape(amount);
            case "rightT" -> Shape.getRightTShape(amount);
            case "leftT" -> Shape.getLeftTShape(amount);
            default -> MehrereKoordinaten(parts[1], amount);
        };
    }

    public static Shape MehrereKoordinaten(String s, int amount) {
        List<Coordinate2D> Koordinaten = new ArrayList<>();
        char last = s.charAt(s.length() - 1);
        StringBuilder sb = new StringBuilder();
        sb.append(last);

        if (!(sb.toString().equals(";"))) {
            throw new InputMismatchException();
        }
        if (s.contains(";")) {
            String[] Koor = s.split(";");
            for (String string : Koor) {
                Koordinaten.add(parseStringToCoordinate(string));
            }
            return new Shape(Koordinaten, amount);
        } else {
            throw new InputMismatchException();

        }
    }
    public static Map<Coordinate2D, GameFieldElement> parseStringToFieldMap(String s){
        HashMap<Coordinate2D, GameFieldElement> hashMap = new HashMap<>();
        String[] row = s.split("\n");
        try {
            List<GameFieldElement> gameFieldElementList = new ArrayList<>();
            List<Coordinate2D> coordinate2DList = new ArrayList<>();

            for (int i = 0; i < row.length; i++) {
                for (int j = 0; j < row[0].length(); j++) {

                    GameFieldElement gameFieldElement = switch (row[i].charAt(j)) { //todo probleme mit set successor:  Cell at (0,0) has no successor
                        case 'b' -> new Cell(new GameFieldItem(GameFieldItemType.BLUE), new Coordinate2D(j, i));
                        case 'g' -> new Cell(new GameFieldItem(GameFieldItemType.GREEN), new Coordinate2D(j, i));
                        case 'r' -> new Cell(new GameFieldItem(GameFieldItemType.RED), new Coordinate2D(j, i));
                        case 'y' -> new Cell(new GameFieldItem(GameFieldItemType.YELLOW), new Coordinate2D(j, i));
                        case 'p' -> new Cell(new GameFieldItem(GameFieldItemType.PURPLE), new Coordinate2D(j, i));
                        case 'B' -> new Cell(new GameFieldItem(GameFieldItemType.BLACK), new Coordinate2D(j, i));
                        case 'o' -> new Cell(new GameFieldItem(GameFieldItemType.ORANGE), new Coordinate2D(j, i));
                        case 'n' -> new Cell(null, new Coordinate2D(j, i));
                        case '+' -> new Fallthrough(new Coordinate2D(j, i));
                        case '#' -> new Block(new Coordinate2D(j, i));
                        default -> throw new InputMismatchException();
                    };

                    hashMap.put(new Coordinate2D(j,i), gameFieldElement);
                    gameFieldElementList.add(gameFieldElement);
                    coordinate2DList.add(new Coordinate2D(j,i));

                }

            }
            int height = row.length;
            int width = row[0].length();
            int heightCounter = 0;
            for (int i = 0; i < gameFieldElementList.size() ; i++) {
                if(heightCounter == 0){
                    if (hashMap.get(coordinate2DList.get(i)).getType() == GameFieldElementType.FALLTHROUGH) {

                        System.out.println("loop 1");
                        throw new InputMismatchException(" 1 FALLTHROUGH");
                    }else if (hashMap.get(coordinate2DList.get(i)).getType() == GameFieldElementType.BLOCK){

                    }
                    else{
                        if(hashMap.get(coordinate2DList.get(i)).getType() == GameFieldElementType.CELL){
                            for (int j = 0; j < height; j++) {
                                if(hashMap.get(coordinate2DList.get(i+width*(j+1))).getType() == GameFieldElementType.BLOCK){
                                    break;
                                }
                                if(hashMap.get(coordinate2DList.get(i+width*(j+1))).getType() == GameFieldElementType.CELL){
                                    hashMap.get(coordinate2DList.get(i)).setSuccessor(hashMap.get(coordinate2DList.get(i+width*(j+1))));
                                    break;
                                }
                            }

                        }
                    }

                } else if (heightCounter == height-1)
                {


                    if (hashMap.get(coordinate2DList.get(i)).getType() == GameFieldElementType.FALLTHROUGH) { //There is no predecessor here since we are in line 0
                        System.out.println("eroor 2");
                        throw new InputMismatchException("2 FALLTHROUGH ");
                    }else if (hashMap.get(coordinate2DList.get(i)).getType() == GameFieldElementType.BLOCK){

                    }
                    else{

                        for (int j = 0; j < height; j++) {
                            System.out.printf("i=%d ,j=%d , Width=%d  \n",i,j,width);
                            if(hashMap.get(coordinate2DList.get(i-width*(j+1))).getType() == GameFieldElementType.BLOCK){
                                break;
                            }
                            if(hashMap.get(coordinate2DList.get(i-width*(j+1))).getType() == GameFieldElementType.CELL){
                                hashMap.get(coordinate2DList.get(i)).setPredecessor(hashMap.get(coordinate2DList.get(i-width*(j+1))));
                                break;
                            }
                        }


                    }
                }
                else{

                    if (gameFieldElementList.get(i).getType() == GameFieldElementType.FALLTHROUGH) {
                        System.out.printf("-2-: i=%d   \n",i);

                        if (gameFieldElementList.get(i + width).getType() == GameFieldElementType.BLOCK || gameFieldElementList.get(i - width).getType() == GameFieldElementType.BLOCK) {
                            throw new InputMismatchException("BLOCK ");
                        }

                    }else if (gameFieldElementList.get(i).getType() == GameFieldElementType.BLOCK){
                        //if it's a block don't do anything
                    }
                    else{
                        if (hashMap.get(coordinate2DList.get(i + width)).getType() == GameFieldElementType.CELL || hashMap.get(coordinate2DList.get(i + width)).getType() == GameFieldElementType.FALLTHROUGH) {
                            for (int j = 0; j < height; j++) {
                                System.out.printf("2-: i=%d ,j=%d  \n",i,j);

                                if(hashMap.get(coordinate2DList.get(i+width*(j+1))).getType() == GameFieldElementType.BLOCK){
                                    break;
                                }
                                if(hashMap.get(coordinate2DList.get(i+width*(j+1))).getType() == GameFieldElementType.CELL){
                                    hashMap.get(coordinate2DList.get(i)).setSuccessor(hashMap.get(coordinate2DList.get(i+width*(j+1))));
                                    break;
                                }
                            }
                        }
                        if (hashMap.get(coordinate2DList.get(i - width)).getType() == GameFieldElementType.CELL || hashMap.get(coordinate2DList.get(i - width)).getType() == GameFieldElementType.FALLTHROUGH) {
                            for (int j = 0; j < height; j++) {
                                System.out.printf("3-: i=%d ,j=%d , Width=%d  \n",i,j,width);

                                if(hashMap.get(coordinate2DList.get(i-width*(j+1))).getType() == GameFieldElementType.BLOCK){
                                    break;
                                }
                                if(hashMap.get(coordinate2DList.get(i-width*(j+1))).getType() == GameFieldElementType.CELL){
                                    hashMap.get(coordinate2DList.get(i)).setPredecessor(hashMap.get(coordinate2DList.get(i-width*(j+1))));
                                    break;
                                }
                            }
                        }
                    }
                }

                if((i+1) % width == 0 && i != 0){
                    heightCounter += 1;
                }

            }

            return hashMap;
        }catch (Exception e){
            throw new InputMismatchException();
        }
    }

}
