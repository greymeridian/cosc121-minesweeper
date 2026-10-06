/**
 * COSC121 Lab 8
 * March 25th 2026
 * @author Ronald J.L. Lorette 300294189
 * @version 1.2
 */

public class TileViewState {

    private boolean revealed;
    private boolean mine;
    private boolean flagged;
    private boolean explodedMine;
    private boolean showMine;
    private boolean misflagged; //indicates a flag placed on a non-mine tile
    private int adjacentMines;

    /**
     * Constructs a TileViewState object representing a snapshot of a tile.
     *
     * @param revealed true if the tile is revealed
     * @param mine true if the tile contains a mine
     * @param flagged true if the tile is flagged
     * @param explodedMine true if the tile is the exploded mine
     * @param showMine true if the tile should display a mine
     * @param misflagged true if the tile is incorrectly flagged
     * @param adjacentMines number of adjacent mines
     */
    public TileViewState(boolean revealed, boolean mine, boolean flagged,
                         boolean explodedMine, boolean showMine,
                         boolean misflagged, int adjacentMines) {
        this.revealed = revealed;
        this.mine = mine;
        this.flagged = flagged;
        this.explodedMine = explodedMine;
        this.showMine = showMine;
        this.misflagged = misflagged; //used by the view to render incorrect flag state
        this.adjacentMines = adjacentMines;
    }

    /**
     * Returns whether the tile is revealed.
     *
     * @return true if revealed, false otherwise
     */
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * Returns whether the tile contains a mine.
     *
     * @return true if the tile contains a mine, false otherwise
     */
    public boolean isMine() {
        return mine;
    }

    /**
     * Returns whether the tile is flagged.
     *
     * @return true if flagged, false otherwise
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * Returns whether the tile is the exploded mine.
     *
     * @return true if exploded mine, false otherwise
     */
    public boolean isExplodedMine() {
        return explodedMine;
    }

    /**
     * Returns whether the tile should display a mine.
     *
     * @return true if the mine should be shown, false otherwise
     */
    public boolean isShowMine() {
        return showMine;
    }

    /**
     * Returns whether the tile is incorrectly flagged.
     *
     * @return true if misflagged, false otherwise
     */
    public boolean isMisflagged() { //used to determine if a special incorrect flag graphic is needed
        return misflagged;
    }

    /**
     * Returns the number of adjacent mines.
     *
     * @return the number of adjacent mines
     */
    public int getAdjacentMines() {
        return adjacentMines;
    }
}