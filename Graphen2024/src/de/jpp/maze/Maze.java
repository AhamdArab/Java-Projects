package de.jpp.maze;

public interface Maze {


    /**
     * Sets the horizontal wall active (non-passable) or inactive (passable)
     *
     * @param x          the x coordinate of the horizontal wall
     * @param y          the y coordinate of the horizontal wall
     * @param wallActive whether the wall is active (non-passable) or not
     */
    void setHWall(int x, int y, boolean wallActive);

    /**
     * Sets the vertical wall active (non-passable) or inactive (passable)
     *
     * @param x          the x coordinate of the vertical wall
     * @param y          the y coordinate of the vertical wall
     * @param wallActive whether the wall is active (non-passable) or not
     */
    void setVWall(int x, int y, boolean wallActive);

    /**
     * Sets all vertical and horizontal walls active (non-passable) or inactive (passable)
     *
     * @param wallActive whether the walls are active (non-passable) or not
     */
    void setAllWalls(boolean wallActive);

    /**
     * returns the width (amount of cells in a row) of this maze
     *
     * @return the width (amount of cells in a row) of this maze
     */
    int getWidth();

    /**
     * returns the height (amount of cells in a column) of this maze
     *
     * @return the height (amount of cells in a column) of this maze
     */
    int getHeight();

    /**
     * Returns whether the horizontal wall at the specified coordinate is active (non-passable) or inactive (passable)
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return whether the horizontal wall at the specified coordinate is active (non-passable) or inactive (passable)
     */
    boolean isHWallActive(int x, int y);

    /**
     * Returns whether the vertical wall at the specified coordinate is active (non-passable) or inactive (passable)
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return whether the vertical wall at the specified coordinate is active (non-passable) or inactive (passable)
     */
    boolean isVWallActive(int x, int y);

}
