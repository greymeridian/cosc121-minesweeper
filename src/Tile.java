/**
 * COSC121 Lab 8
 * March 25th 2026
 * @author Ronald J.L. Lorette 300294189
 * @version 1.2
 */

public class Tile {

    private boolean mine;
    private boolean revealed;
    private boolean flagged;
    private boolean explodedMine;
    private boolean showMine;
    private boolean misflagged; //tracks a flagged tile that is not actually a mine
    private int adjacentMines;

    /**
     * Constructs a Tile object with default values.
     */
    public Tile() {
        mine = false;
        revealed = false;
        flagged = false;
        explodedMine = false;
        showMine = false;
        misflagged = false; //starts false until a wrong flag is detected
        adjacentMines = 0;
    }

    /**
     * Sets whether this tile contains a mine.
     *
     * @param mine true if this tile contains a mine, false otherwise
     */
    public void setMine(boolean mine) {
        this.mine = mine;
    }

    /**
     * Returns whether this tile contains a mine.
     *
     * @return true if this tile contains a mine, false otherwise
     */
    public boolean isMine() {
        return mine;
    }

    /**
     * Sets whether this tile is revealed.
     *
     * @param revealed true if this tile is revealed, false otherwise
     */
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    /**
     * Returns whether this tile is revealed.
     *
     * @return true if this tile is revealed, false otherwise
     */
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * Sets whether this tile is flagged.
     *
     * @param flagged true if this tile is flagged, false otherwise
     */
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    /**
     * Returns whether this tile is flagged.
     *
     * @return true if this tile is flagged, false otherwise
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * Sets whether this tile is the exploded mine.
     *
     * @param explodedMine true if this tile is the exploded mine, false otherwise
     */
    public void setExplodedMine(boolean explodedMine) {
        this.explodedMine = explodedMine;
    }

    /**
     * Returns whether this tile is the exploded mine.
     *
     * @return true if this tile is the exploded mine, false otherwise
     */
    public boolean isExplodedMine() {
        return explodedMine;
    }

    /**
     * Sets whether this tile should display a mine.
     *
     * @param showMine true if this tile should display a mine, false otherwise
     */
    public void setShowMine(boolean showMine) {
        this.showMine = showMine;
    }

    /**
     * Returns whether this tile should display a mine.
     *
     * @return true if this tile should display a mine, false otherwise
     */
    public boolean isShowMine() {
        return showMine;
    }

    /**
     * Sets whether this tile is misflagged.
     *
     * @param misflagged true if this tile is a wrong flag, false otherwise
     */
    public void setMisflagged(boolean misflagged) { //stores whether the placed flag was incorrect
        this.misflagged = misflagged;
    }

    /**
     * Returns whether this tile is misflagged.
     *
     * @return true if this tile is misflagged, false otherwise
     */
    public boolean isMisflagged() { //lets the view render a misflagged tile image
        return misflagged;
    }

    /**
     * Sets the number of adjacent mines.
     *
     * @param adjacentMines the number of adjacent mines
     */
    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
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