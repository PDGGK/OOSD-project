package game;

import bagel.Font;
import bagel.Window;

/**
 * Manages the scoring system for the game.
 * Handles score calculation, updates, and display.
 */
public class ScoreManager {
    // Score constants
    private static final int BARREL_JUMP_SCORE = 30;
    private static final int BARREL_DESTROY_SCORE = 100;
    private static final int MONKEY_DESTROY_SCORE = 100;
    private static final int TIME_BONUS_MULTIPLIER = 3;
    
    private int score;
    private boolean timeBonus = false;
    private int timeBonusScore = 0;
    
    /**
     * Creates a new score manager with initial score of 0.
     */
    public ScoreManager() {
        this.score = 0;
    }
    
    /**
     * Adds points to the current score.
     *
     * @param points The number of points to add
     */
    public void addScore(int points) {
        score += points;
    }
    
    /**
     * Gets the current score.
     * If time bonus has been applied, includes it in the total.
     *
     * @return The current score including any time bonus
     */
    public int getScore() {
        return score + timeBonusScore;
    }
    
    /**
     * Calculates and adds the time bonus to the score.
     * Time bonus is calculated as (remaining seconds × 3).
     * This should be called when the game ends.
     *
     * @param remainingSeconds The number of seconds remaining
     */
    public void addTimeBonus(int remainingSeconds) {
        if (!timeBonus) {  // Only apply time bonus once
            timeBonusScore = remainingSeconds * TIME_BONUS_MULTIPLIER;
            timeBonus = true;
        }
    }
    
    /**
     * Checks if the time bonus has already been calculated.
     *
     * @return true if time bonus has been applied, false otherwise
     */
    public boolean hasTimeBonus() {
        return timeBonus;
    }
    
    /**
     * Gets the time bonus score.
     *
     * @return The time bonus score
     */
    public int getTimeBonus() {
        return timeBonusScore;
    }
    
    /**
     * Adds score for jumping over a barrel.
     */
    public void addBarrelJumpScore() {
        addScore(BARREL_JUMP_SCORE);
    }
    
    /**
     * Adds score for destroying a barrel.
     */
    public void addBarrelDestroyScore() {
        addScore(BARREL_DESTROY_SCORE);
    }
    
    /**
     * Adds score for destroying a monkey.
     */
    public void addMonkeyDestroyScore() {
        addScore(MONKEY_DESTROY_SCORE);
    }
    
    /**
     * Resets the score to 0 and clears any time bonus.
     */
    public void reset() {
        score = 0;
        timeBonusScore = 0;
        timeBonus = false;
    }
    
    /**
     * Loads a previously saved score (for level transitions).
     *
     * @param savedScore The score to load
     */
    public void loadSavedScore(int savedScore) {
        this.score = savedScore;
    }
    
    /**
     * Gets the score before any time bonus is applied.
     * Used for carrying score between levels.
     *
     * @return The base score without time bonus
     */
    public int getBaseScore() {
        return score;
    }
} 