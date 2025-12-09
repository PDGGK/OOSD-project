import bagel.*;
import game.GameState;
import game.LevelManager;
import screens.GameplayScreen;
import screens.Level1Screen;
import screens.Level2Screen;
import screens.Screen;
import screens.TitleScreen;
import screens.GameOverScreen;

import java.util.Properties;

/**
 * The main class for the Shadow Donkey Kong game.
 * This class extends {@code AbstractGame} and is responsible for managing game initialization,
 * updates, rendering, and handling user input.
 *
 * It sets up the game world, initializes screens, and handles state transitions.
 */
public class ShadowDonkeyKong extends AbstractGame {

    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    
    // Game state
    private GameState currentState;
    
    // Level management
    private final LevelManager levelManager;
    
    // Screens
    private final TitleScreen titleScreen;
    private GameplayScreen gameplayScreen;
    
    // Current active screen
    private Screen activeScreen;


    public ShadowDonkeyKong(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                messageProps.getProperty("home.title"));

        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;
        
        // Initialize level manager
        this.levelManager = new LevelManager(GAME_PROPS, MESSAGE_PROPS);
        
        // Initialize screens
        titleScreen = new TitleScreen(GAME_PROPS, MESSAGE_PROPS);
        // Initialize with level 1 screen by default
        gameplayScreen = new Level1Screen(GAME_PROPS, MESSAGE_PROPS);
        
        // Set initial state to title screen
        currentState = GameState.TITLE;
        activeScreen = titleScreen;
    }


    /**
     * The update method is called by the Bagel engine once per frame.
     * It handles input and updates the current screen.
     *
     * @param input Current input state
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        
        if (activeScreen != null) {
            GameState newState = activeScreen.update(input);
            
            if (newState != null && newState != currentState) {
                handleStateTransition(newState);
            }
        }
        
        // Draw the active screen
        if (activeScreen != null) {
            activeScreen.draw();
        }
    }

    /**
     * Handle transitions between game states.
     * Updates the active screen based on the new state.
     * 
     * @param newState The new game state to transition to
     */
    private void handleStateTransition(GameState newState) {
        // Record the current score, to maintain the score during level transitions
        int currentScore = 0;
        int timeBonus = 0;
        if (gameplayScreen != null) {
            currentScore = gameplayScreen.getScore();
            // Get time bonus from the score manager
            timeBonus = gameplayScreen.getScoreManager().getTimeBonus();
        }
        
        currentState = newState;
        
        // Set the appropriate screen for the new state
        switch (currentState) {
            case TITLE:
                activeScreen = titleScreen;
                break;
            case LEVEL1:
                levelManager.reset(); // Reset level manager when starting level 1
                gameplayScreen = new Level1Screen(GAME_PROPS, MESSAGE_PROPS);
                activeScreen = gameplayScreen;
                break;
            case LEVEL2:
                // If transition from Level1 to Level2 (advancing to next level)
                if (activeScreen instanceof Level1Screen) {
                    levelManager.advanceToNextLevel();
                    gameplayScreen = new Level2Screen(GAME_PROPS, MESSAGE_PROPS);
                    
                    // Transfer ONLY the base score from level 1 to level 2 (excluding time bonus)
                    gameplayScreen.getScoreManager().addScore(currentScore - timeBonus);
                    
                    // Level 2 will calculate its own time bonus independently
                    activeScreen = gameplayScreen;
                } 
                // If you enter the second level directly from the title screen
                else if (activeScreen instanceof TitleScreen) {
                    // Enter level 2 directly from the title screen
                    levelManager.startAtLevel2();
                    gameplayScreen = new Level2Screen(GAME_PROPS, MESSAGE_PROPS);
                    activeScreen = gameplayScreen;
                }
                break;
            case GAME_OVER_WIN:
                // When you win, keep the final score, including time rewards
                int finalWinScore = gameplayScreen.getScore();
                activeScreen = new GameOverScreen(GAME_PROPS, MESSAGE_PROPS, currentState, finalWinScore);
                break;
            case GAME_OVER_LOSE:
                // Set the score to 0 when failed (according to the specification requirements of project 2)
                activeScreen = new GameOverScreen(GAME_PROPS, MESSAGE_PROPS, currentState, 0);
                break;
        }
    }


    /**
     * The main entry point of the Shadow Donkey Kong game.
     *
     * This method loads the game properties and message files, initializes the game,
     * and starts the game loop.
     *
     * @param args Command-line arguments (not used in this game).
     */
    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile("res/app.properties");
        Properties messageProps = IOUtils.readPropertiesFile("res/message.properties");
        ShadowDonkeyKong game = new ShadowDonkeyKong(gameProps, messageProps);
        game.run();
    }


}
