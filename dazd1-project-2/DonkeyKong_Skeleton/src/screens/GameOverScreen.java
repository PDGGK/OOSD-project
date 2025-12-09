package screens;

import bagel.*;
import game.GameState;

import java.util.Properties;

/**
 * The game over screen shown when the game ends.
 * Displays the win/lose message, final score, and a prompt to continue.
 */
public class GameOverScreen extends Screen {
    // All layout positions now come from app.properties configuration
    
    private final Font statusFont;
    private final Font scoreFont;
    private final int statusY;
    private final int scoreY;
    private final String winMessage;
    private final String loseMessage;
    private final String continueMessage;
    
    private final GameState result;
    private final int finalScore;
    
    /**
     * Creates a new game over screen.
     *
     * @param gameProps Properties containing game configuration
     * @param messageProps Properties containing game messages
     * @param result The game result (win or lose)
     * @param score The final score
     */
    public GameOverScreen(Properties gameProps, Properties messageProps, GameState result, int score) {
        super(gameProps, messageProps);
        
        this.result = result;
        this.finalScore = score;
        
        // Load font settings using parent class method
        this.statusFont = createFont("gameEnd.status.fontSize");
        this.scoreFont = createFont("gameEnd.scores.fontSize");
        
        // Load position settings
        this.statusY = Integer.parseInt(gameProps.getProperty("gameEnd.status.y"));
        this.scoreY = Integer.parseInt(gameProps.getProperty("gameEnd.scores.y"));
        
        // Load messages
        this.winMessage = messageProps.getProperty("gameEnd.won");
        this.loseMessage = messageProps.getProperty("gameEnd.lost");
        this.continueMessage = messageProps.getProperty("gameEnd.continue");
    }
    
    /**
     * Updates the game over screen and checks for user input.
     *
     * @param input Current input state
     * @return TITLE if SPACE is pressed, otherwise null (stay on game over screen)
     */
    @Override
    public GameState update(Input input) {
        // Return to title screen when SPACE is pressed
        if (input.wasPressed(Keys.SPACE)) {
            return GameState.TITLE;
        }
        
        // Stay on game over screen
        return null;
    }
    
    /**
     * Draws the game over screen.
     * Shows the game result, final score, and continue prompt.
     */
    @Override
    public void draw() {
        // Draw background using parent class method
        drawBackgroundImage();
        
        // Determine which message to show
        String statusMessage = (result == GameState.GAME_OVER_WIN) ? winMessage : loseMessage;
        
        // Draw status message (centered horizontally at statusY)
        double messageWidth = statusFont.getWidth(statusMessage);
        double centerX = Window.getWidth()/2.0 - messageWidth/2.0;
        statusFont.drawString(statusMessage, centerX, statusY);
        
        // Draw score (centered horizontally at scoreY from app.properties)
        String scoreText = getMessageProps().getProperty("gameEnd.score") + " " + finalScore;
        messageWidth = scoreFont.getWidth(scoreText);
        centerX = Window.getWidth()/2.0 - messageWidth/2.0;
        scoreFont.drawString(scoreText, centerX, scoreY);
        
        // Draw continue prompt (centered horizontally)
        messageWidth = statusFont.getWidth(continueMessage);
        centerX = Window.getWidth()/2.0 - messageWidth/2.0;
        statusFont.drawString(continueMessage, centerX, Window.getHeight() - 100);
    }
} 