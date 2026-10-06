/**
 * COSC121 Lab 8
 * March 25th 2026
 * @author Ronald J.L. Lorette 300294189
 * @version 1.2
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class HighScoreManager {

    private static final String FILE_NAME = "res/highscores.txt"; //stores highscores in the res folder
    private static final int MAX_SCORES = 5; //maximum saved scores per level

    private Map<Levels, ArrayList<ScoreEntry>> scores; //stores a list of scores for each difficulty level

    /**
     * Constructs a HighScoreManager and loads saved highscore data.
     */
    public HighScoreManager() {
        scores = new EnumMap<>(Levels.class);

        for (Levels level : Levels.values()) {
            scores.put(level, new ArrayList<ScoreEntry>()); //each level gets its own score list
        }

        ensureFileExists();
        loadScores();
    }

    /**
     * Ensures the highscore file exists before reading or writing.
     */
    private void ensureFileExists() {
        try {
            File file = new File(FILE_NAME);

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        }
        catch (IOException exception) {
            System.out.println("Error creating highscores file: " + exception.getMessage());
        }
    }

    /**
     * Loads saved highscores from the file into memory.
     */
    public void loadScores() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        for (Levels level : Levels.values()) {
            scores.get(level).clear(); //clears old in-memory data before reloading
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();

            while (line != null) {
                String[] parts = line.split(",", 3);

                if (parts.length == 3) {
                    Levels level = Levels.valueOf(parts[0]);
                    String name = parts[1];
                    int time = Integer.parseInt(parts[2]);

                    scores.get(level).add(new ScoreEntry(name, time)); //rebuilds saved score entries from file data
                }

                line = reader.readLine();
            }
        }
        catch (IOException | IllegalArgumentException exception) {
            System.out.println("Error loading highscores: " + exception.getMessage());
        }
    }

    /**
     * Saves all current highscores to the file.
     */
    public void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Levels level : Levels.values()) {
                ArrayList<ScoreEntry> levelScores = scores.get(level);

                for (int index = 0; index < levelScores.size(); index++) {
                    ScoreEntry entry = levelScores.get(index);
                    writer.write(level.name() + "," + entry.getName() + "," + entry.getTime());
                    writer.newLine();
                }
            }
        }
        catch (IOException exception) {
            System.out.println("Error saving highscores: " + exception.getMessage());
        }
    }

    /**
     * Returns whether a given time qualifies as a new highscore for a level.
     *
     * @param level the difficulty level being checked
     * @param time the completion time in seconds
     * @return true if the time qualifies for the top score list, otherwise false
     */
    public boolean isHighScore(Levels level, int time) {
        ArrayList<ScoreEntry> levelScores = scores.get(level);

        if (levelScores.size() < MAX_SCORES) {
            return true;
        }

        return time < levelScores.get(levelScores.size() - 1).getTime(); //compares against the current slowest saved score
    }

    /**
     * Adds a score, sorts it into the correct position, trims the list to the top 5,
     * and saves the updated results.
     *
     * @param level the difficulty level being updated
     * @param name the player name for the score
     * @param time the completion time in seconds
     */
    public void updateScore(Levels level, String name, int time) {
        ArrayList<ScoreEntry> levelScores = scores.get(level);
        ScoreEntry newEntry = new ScoreEntry(name, time);

        int index = 0;

        while (index < levelScores.size() &&
               levelScores.get(index).getTime() <= time) {
            index++;
        }

        levelScores.add(index, newEntry); //inserts the new score in sorted order

        if (levelScores.size() > MAX_SCORES) {
            levelScores.remove(levelScores.size() - 1); //removes the slowest score if more than 5 exist
        }

        saveScores();
    }

    /**
     * Returns all saved scores for a level.
     *
     * @param level the difficulty level to retrieve
     * @return the list of saved scores for that level
     */
    public ArrayList<ScoreEntry> getScores(Levels level) {
        return scores.get(level);
    }

    /**
     * Returns the best score for a level.
     *
     * @param level the difficulty level to retrieve
     * @return the best score entry for that level, or null if none exists
     */
    public ScoreEntry getScore(Levels level) {
        ArrayList<ScoreEntry> levelScores = scores.get(level);

        if (levelScores.isEmpty()) {
            return null;
        }

        return levelScores.get(0);
    }
}