package game;

import bagel.Font;
import bagel.Window;

/**
 * Manages the scoring system for the game.
 * Handles score calculation, updates, and display.
 */
public class ScoreManager {
    private static final double SCORE_X = 100;
    private static final double SCORE_Y = 100;
    
    // Score constants
    private static final int BARREL_JUMP_SCORE = 30;
    private static final int BARREL_DESTROY_SCORE = 100;
    private static final int TIME_BONUS_MULTIPLIER = 3;
    
    private int score;
    private final Font scoreFont;
    private boolean timeBonus = false;
    private int timeBonusScore = 0;
    
    /**
     * Creates a new score manager with initial score of 0.
     */
    public ScoreManager() {
        this.score = 0;
        this.scoreFont = new Font("res/FSO8BITR.TTF", 20);
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
     * Gets the time bonus score.
     *
     * @return The time bonus score
     */
    public int getTimeBonus() {
        return timeBonusScore;
    }
    
    /**
     * Draws the current score on the screen.
     */
    public void draw() {
        String scoreText = "SCORE " + getScore();
        scoreFont.drawString(scoreText, SCORE_X, SCORE_Y);
    }
    
    /**
     * Resets the score to 0 and clears any time bonus.
     */
    public void reset() {
        score = 0;
        timeBonusScore = 0;
        timeBonus = false;
    }
} 