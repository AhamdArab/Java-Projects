package jpp.numbergame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NumberGame {
    private int width;
    private int height;
    private Tile[][] board;
    private boolean[][] mergeTiles;
    private int points;

    public NumberGame(int width, int height) {
        if (width < 1 || height < 1)
            throw new IllegalArgumentException();
        this.width = width;
        this.height = height;
        board = new Tile[height][width];
        mergeTiles = new boolean[height][width];
    }

    public NumberGame(int width, int height, int initialTiles) {
        this(width, height);
        if (initialTiles < 0 || initialTiles > width * height)
            throw new IllegalArgumentException();
        while (initialTiles-- > 0) {
            Tile tile = addRandomTile();
            board[tile.getCoordinate().getY()][tile.getCoordinate().getX()] = tile;
        }
    }

    public int get(Coordinate2D coordinate2D) {
        int x = coordinate2D.getX();
        int y = coordinate2D.getY();
        return get(x, y);
    }

    public int get(int x, int y) {

        return board[y][x] == null ? 0 : board[y][x].getValue();
    }

    public int getPoints() {
        return points;
    }

    public boolean isFull() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (get(j, i) == 0)
                    return false;
        return true;
    }

    public Tile addRandomTile() {
        if (isFull())
            throw new TileExistsException();
        Random random = new Random();
        int x;
        int y;
        do {
            x = random.nextInt(width);
            y = random.nextInt(height);
        } while (get(x, y) > 0);
        int randomNumber = random.nextInt(10);
        int value = (randomNumber < 1) ? 4 : 2;
        return addTile(x, y, value);
    }

    public Tile addTile(int x, int y, int value) {
        Tile tile = new Tile(new Coordinate2D(x, y), value);
        if (get(x, y) > 0)
            throw new TileExistsException();
        return board[y][x] = tile;
    }



    public boolean canMove(Direction dir) {
        int rowOffset = getRowOffset(dir);
        int colOffset = getColOffset(dir);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int tileValue = get(x, y);
                if (tileValue == 0)
                    continue;
                try {
                    int newX = x + colOffset;
                    int newY = y + rowOffset;
                    int neighborTileValue = get(newX, newY);
                    if (neighborTileValue == 0 || neighborTileValue == tileValue)
                        return true;
                } catch (IndexOutOfBoundsException ignored) {

                }
            }
        }
        return false;
    }

    public boolean canMove() {
        for (Direction direction : Direction.values()){
            if (canMove(direction))
                return true;
        }
        return false;
    }
    public List<Move> move(Direction dir) {
        if (!canMove(dir))
            return new ArrayList<>();
        List<Move> moves = new ArrayList<>();
        int changeY = dir.equals(Direction.DOWN) ? -1 : 1;
        int changeX = dir.equals(Direction.RIGHT) ? -1 : 1;
        int rowOffset = getRowOffset(dir);
        int colOffset = getColOffset(dir);

        int y = dir.equals(Direction.DOWN) ? height - 1 : 0;
        while (y >= 0 && y < height) {
            int x = dir.equals(Direction.RIGHT) ? width - 1 : 0;
            while (x >= 0 && x < width) {
                int tileValue = get(x, y);
                if (tileValue == 0) {
                    x = x + changeX;
                    continue;
                }
                int oldX = x;
                int oldY = y;
                int newX = x + colOffset;
                int newY = y + rowOffset;
                Coordinate2D from = new Coordinate2D(x, y);
                Coordinate2D to = new Coordinate2D(x, y);
                do {
                    try {
                        int newTileValue = get(newX, newY);
                        if (newTileValue == 0) {
                            board[oldY][oldX] = null;
                            to = new Coordinate2D(newX, newY);
                            board[newY][newX] = new Tile(to, tileValue);

                        } else if (newTileValue == tileValue && !getMergeTile(newX, newY)) {
                            board[oldY][oldX] = null;
                            to = new Coordinate2D(newX, newY);
                            board[newY][newX] = new Tile(to, tileValue * 2);
                            setMergeTile(newX, newY, true);
                            points = points + (tileValue * 2);
                        }else if (newTileValue != tileValue){
                            int newValue = getMergeTile(to.getX(), to.getY()) ? tileValue * 2 : tileValue;
                            if (!from.equals(to))
                                moves.add(new Move(from, to, tileValue, newValue));
                            break;
                        }
                        oldX = newX;
                        oldY = newY;
                        newX = newX + colOffset;
                        newY = newY + rowOffset;
                    } catch (IndexOutOfBoundsException exception) {
                        int newValue = getMergeTile(to.getX(), to.getY()) ? tileValue * 2 : tileValue;
                        if (!from.equals(to))
                            moves.add(new Move(from, to, tileValue, newValue));
                        break;
                    }
                } while (true);
                x = x + changeX;
            }
            y = y + changeY;

        }

        resetMergeTiles();
        return moves;
    }

    public int getRowOffset(Direction direction) {
        switch (direction) {
            case RIGHT:
            case LEFT:
                return 0;
            case UP:
                return -1;
            case DOWN:
                return 1;
        }
        return 0;
    }

    public int getColOffset(Direction direction) {
        switch (direction) {
            case RIGHT:
                return 1;
            case LEFT:
                return -1;
            case UP:
            case DOWN:
                return 0;
        }
        return 0;
    }

    private void setMergeTile(int x, int y, boolean value) {
        mergeTiles[y][x] = value;
    }

    private boolean getMergeTile(int x, int y) {
        return mergeTiles[y][x];
    }

    private void resetMergeTiles() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                setMergeTile(x, y, false);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                stringBuilder.append(get(j, i)).append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
