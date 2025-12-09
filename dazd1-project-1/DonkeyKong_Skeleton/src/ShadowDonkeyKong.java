import bagel.*;
import game.GameState;
import screens.GameplayScreen;
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
        
        // Initialize screens
        titleScreen = new TitleScreen(GAME_PROPS, MESSAGE_PROPS);
        gameplayScreen = new GameplayScreen(GAME_PROPS, MESSAGE_PROPS);
        
        // Set initial state to title screen
        currentState = GameState.TITLE;
        activeScreen = titleScreen;
    }


    /**
     * Update the game state and render the current screen.
     * Handles state transitions between different screens.
     * 
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        
        // Update the active screen and check for state transitions
        GameState newState = activeScreen.update(input);
        
        // If the screen requested a state change
        if (newState != null && newState != currentState) {
            handleStateTransition(newState);
        }
        
        // Draw the active screen
        activeScreen.draw();
    }

    /**
     * Handle transitions between game states.
     * Updates the active screen based on the new state.
     * 
     * @param newState The new game state to transition to
     */
    private void handleStateTransition(GameState newState) {
        currentState = newState;
        
        // Set the appropriate screen for the new state
        switch (currentState) {
            case TITLE:
                activeScreen = titleScreen;
                break;
            case PLAYING:
                // Create a new gameplay screen instance each time to reset game state
                gameplayScreen = new GameplayScreen(GAME_PROPS, MESSAGE_PROPS);
                activeScreen = gameplayScreen;
                break;
            case GAME_OVER_WIN:
            case GAME_OVER_LOSE:
                // Create game over screen with the final score
                int finalScore = gameplayScreen.getScore();
                activeScreen = new GameOverScreen(GAME_PROPS, MESSAGE_PROPS, currentState, finalScore);
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
