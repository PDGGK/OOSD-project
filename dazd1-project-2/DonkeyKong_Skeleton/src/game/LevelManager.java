package game;

import java.util.Properties;

/**
 * Manages level transitions and state across multiple levels.
 * Responsible for tracking score between levels and handling level completion.
 */
public class LevelManager {
    private final ScoreManager scoreManager;
    private GameState currentLevel;
    private final Properties gameProps;
    private final Properties messageProps;
    
    /**
     * Creates a new level manager.
     * 
     * @param gameProps Properties containing game configuration
     * @param messageProps Properties containing game messages
     */
    public LevelManager(Properties gameProps, Properties messageProps) {
        this.scoreManager = new ScoreManager();
        this.currentLevel = GameState.LEVEL1; // Default to level 1
        this.gameProps = gameProps;
        this.messageProps = messageProps;
    }
    
    /**
     * Gets the current level state.
     * 
     * @return The current level state (LEVEL1 or LEVEL2)
     */
    public GameState getCurrentLevel() {
        return currentLevel;
    }
    
    /**
     * Sets the current level.
     * 
     * @param level The level to set as current
     */
    public void setCurrentLevel(GameState level) {
        if (level == GameState.LEVEL1 || level == GameState.LEVEL2) {
            this.currentLevel = level;
        } else {
            throw new IllegalArgumentException("Invalid level state: " + level);
        }
    }
    
    /**
     * Gets the score manager.
     * 
     * @return The score manager
     */
    public ScoreManager getScoreManager() {
        return scoreManager;
    }
    
    /**
     * Advances to the next level.
     * If currently in level 1, advances to level 2.
     * If currently in level 2, does nothing.
     * 
     * @return The new level state, or null if already at the final level
     */
    public GameState advanceToNextLevel() {
        // Only advance if current level is LEVEL1
        if (currentLevel == GameState.LEVEL1) {
            // When advancing levels, maintain the score but don't apply time bonus yet
            // Time bonus will be calculated at the end of level 2 or if player loses
            currentLevel = GameState.LEVEL2;
            return currentLevel;
        }
        
        // Already at the final level
        return null;
    }
    
    /**
     * Resets the level manager to start a new game.
     * Clears all scores and sets the level to LEVEL1.
     */
    public void reset() {
        scoreManager.reset();
        currentLevel = GameState.LEVEL1;
    }
    
    /**
     * Starts directly at level 2.
     * Resets the score as this is starting a new game.
     */
    public void startAtLevel2() {
        scoreManager.reset();
        currentLevel = GameState.LEVEL2;
    }
    
    /**
     * Gets the properties for the current level.
     * 
     * @return The game properties
     */
    public Properties getGameProps() {
        return gameProps;
    }
    
    /**
     * Gets the message properties.
     * 
     * @return The message properties
     */
    public Properties getMessageProps() {
        return messageProps;
    }
} 