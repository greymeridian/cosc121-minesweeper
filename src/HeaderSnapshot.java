/**
 * COSC121 Lab 8
 * March 25th 2026
 * @author Ronald J.L. Lorette 300294189
 * @version 1.2
 */

public class HeaderSnapshot {

    private FaceState faceState;
    private int bombsLeft;
    private int elapsedTime;

    /**
     * Constructs a HeaderSnapshot representing the current header state.
     *
     * @param faceState the current face state to display
     * @param bombsLeft the number of bombs remaining (mine count minus flags placed)
     * @param elapsedTime the elapsed time in seconds
     */
    public HeaderSnapshot(FaceState faceState, int bombsLeft, int elapsedTime) {
        this.faceState = faceState;
        this.bombsLeft = bombsLeft;
        this.elapsedTime = elapsedTime;
    }

    /**
     * Returns the current face state.
     *
     * @return the face state
     */
    public FaceState getFaceState() {
        return faceState;
    }

    /**
     * Returns the number of bombs remaining.
     *
     * @return the number of bombs left
     */
    public int getBombsLeft() {
        return bombsLeft;
    }

    /**
     * Returns the elapsed time.
     *
     * @return the elapsed time in seconds
     */
    public int getElapsedTime() {
        return elapsedTime;
    }
}