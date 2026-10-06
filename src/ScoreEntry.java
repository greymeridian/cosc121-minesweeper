/**
 * COSC121 Lab 8
 * March 25th 2026
 * @author Ronald J.L. Lorette 300294189
 * @version 1.2
 */

public class ScoreEntry {

    private String name; //stores the player name associated with the score
    private int time; //stores the completion time in seconds

    /**
     * Constructs a ScoreEntry with a player name and completion time.
     *
     * @param name the name of the player
     * @param time the completion time in seconds
     */
    public ScoreEntry(String name, int time) {
        this.name = name;
        this.time = time;
    }

    /**
     * Returns the player's name.
     *
     * @return the player name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the completion time.
     *
     * @return the time in seconds
     */
    public int getTime() {
        return time;
    }
}