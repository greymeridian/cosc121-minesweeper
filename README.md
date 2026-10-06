# Minesweeper

A JavaFX implementation of the classic Minesweeper game created for **COSC 121 Lab 8** in March 2026.

The project recreates traditional Minesweeper gameplay with multiple difficulty levels, recursive tile revealing, flagging, chord reveals, a timer, and persistent high scores.

## Gameplay

The objective is to reveal every tile that does not contain a mine.

- **Left-click** a covered tile to reveal it.
- **Right-click** a covered tile to place or remove a flag.
- Revealing an empty tile recursively reveals surrounding empty areas.
- Clicking an already revealed numbered tile performs a **chord reveal** when the number of adjacent flags matches the tile's number.
- Revealing a mine ends the game.
- Revealing all non-mine tiles wins the game.
- The face button starts a new game.

When a game is lost, mines are revealed and incorrectly placed flags are identified.

## Difficulty Levels

The game provides three selectable difficulty levels:

- Beginner
- Intermediate
- Expert

A difficulty selection dialog is displayed when the application starts.

## Features

- JavaFX graphical interface
- Beginner, Intermediate, and Expert difficulty levels
- Random mine placement
- Recursive empty-tile revealing
- Chord revealing
- Tile flagging
- Incorrect flag detection
- Mine counter
- Game timer
- Win and loss states
- Classic Minesweeper-style face button
- Persistent high scores
- Top five scores stored separately for each difficulty
- Player name entry for qualifying high scores

## Project Structure

```text
Lab8/
├── src/                    # Java source files
│   ├── Minesweeper.java
│   ├── MinesweeperController.java
│   ├── MinesweeperEngine.java
│   ├── MinesweeperView.java
│   ├── Tile.java
│   ├── TileViewState.java
│   ├── BoardConfig.java
│   ├── BoardSnapshot.java
│   ├── HeaderSnapshot.java
│   ├── HighScoreManager.java
│   ├── ScoreEntry.java
│   ├── AssetLoader.java
│   ├── Levels.java
│   ├── GameState.java
│   ├── FaceState.java
│   └── TileClickHandler.java
├── res/
│   ├── digits/             # Counter digit graphics
│   ├── minesweeper-basic/  # Board, mine, flag and face graphics
│   └── highscores.txt      # Saved high-score data
└── bin/                    # Compiled Java classes
```

## Architecture

The project separates the game into several components:

- `Minesweeper` launches the JavaFX application and handles initial difficulty selection.
- `MinesweeperController` coordinates the game engine and user interface.
- `MinesweeperEngine` contains the core game rules, board state, mine placement, revealing, flagging, and win/loss logic.
- `MinesweeperView` builds and updates the JavaFX interface.
- `Tile` represents the state of an individual board tile.
- `HighScoreManager` loads and saves the five fastest completion times for each difficulty level.
- Snapshot and view-state classes transfer game state from the engine to the interface.

## Running the Project

The application requires Java with JavaFX available.

Compile the source with JavaFX on the module path, then run:

```bash
java Minesweeper
```

The application starts by prompting for a difficulty level before opening the game board.

## High Scores

The five fastest completion times for each difficulty are stored in:

```text
res/highscores.txt
```

When a winning time qualifies for the high-score list, the player is prompted to enter a name. Scores are maintained separately for Beginner, Intermediate, and Expert.

## Notes

This repository preserves the project as an old COSC 121 lab submission. It is not intended to represent the structure or coding practices of a current production Java application.
