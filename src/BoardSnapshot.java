/**
 * COSC121 Lab 8
 * March 25th 2026
 * @author Ronald J.L. Lorette 300294189
 * @version 1.2
 */

public class BoardSnapshot {

    private TileViewState[][] tiles;
    private int rows;
    private int cols;

    /**
     * Constructs a BoardSnapshot representing a read-only view of the board state.
     *
     * @param tiles a 2D array of TileViewState objects representing the board
     * @param rows the number of rows in the board
     * @param cols the number of columns in the board
     */
    public BoardSnapshot(TileViewState[][] tiles, int rows, int cols) {
        this.tiles = tiles;
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Returns the 2D array of tile view states.
     *
     * @return the tile view state grid
     */
    public TileViewState[][] getTiles() {
        return tiles;
    }

    /**
     * Returns the number of rows in the board.
     *
     * @return the number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns in the board.
     *
     * @return the number of columns
     */
    public int getCols() {
        return cols;
    }
}