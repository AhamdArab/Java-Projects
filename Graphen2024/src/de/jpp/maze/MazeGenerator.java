package de.jpp.maze;

import java.util.Random;
import java.util.stream.IntStream;

public class MazeGenerator {

    boolean[][] maze;
    int width;
    int height;
    int randomCallsCount;

    public MazeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.maze = new boolean[height * 2 - 1][width * 2 - 1];
        this.randomCallsCount = 0;
    }

    public boolean[][] generateMaze(Random random) {
        generate(random, 0, (2 * width - 1) - 1, 0, (2 * height - 1) - 1);
        return maze;
    }

    public void generate(Random random, int left, int right, int top, int bottom) {

        if (right - left > bottom - top) {

            divideVerticallyAndGenerate(random, left, right, top, bottom);
        }
        else if(right - left < bottom - top) {

            divideHorizontallyAndGenerate(random, left, right, top, bottom);

        } else {
            if (nextBoolean(random)) {

                divideVerticallyAndGenerate(random, left, right, top, bottom);

            } else {

                divideHorizontallyAndGenerate(random, left, right, top, bottom);

            }
        }
    }

    private void divideVerticallyAndGenerate(Random random, int left, int right, int top, int bottom) {
        int wallX = left + (nextInt(random, (right - left) / 2) * 2) + 1;
        int passageY = top + nextInt(random, (((bottom - top) / 2) + 1) - 1) * 2;

        IntStream.rangeClosed(top, bottom)
                .filter(y -> y != passageY && y % 2 == 0)
                .forEach(y -> maze[y][wallX] = true);

        if (wallX - left > 1) {
            generate(random, left, wallX - 1, top, bottom);
        }

        if (right - wallX > 1) {
            generate(random, wallX + 1, right, top, bottom);
        }
    }

    private void divideHorizontallyAndGenerate(Random random, int left, int right, int top, int bottom) {
        int wallY = top + (nextInt(random, (bottom - top) / 2) * 2) + 1;
        int passageX = left + nextInt(random, (((right - left) / 2) + 1) - 1) * 2;

        IntStream.iterate(left, i -> i <= right, i -> i + 2)
                .filter(i -> i != passageX)
                .forEach(i -> maze[wallY][i] = true);

        if (wallY - top > 1) {
            generate(random, left, right, top, wallY - 1);
        }

        if (bottom - wallY > 1) {
            generate(random, left, right, wallY + 1, bottom);
        }
    }

    private int nextInt(Random random, int bound) {
        randomCallsCount++;
        return random.nextInt(bound);
    }

    private boolean nextBoolean(Random random) {
        randomCallsCount++;
        return random.nextBoolean();
    }
}
