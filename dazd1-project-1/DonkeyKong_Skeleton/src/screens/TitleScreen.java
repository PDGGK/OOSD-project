package screens;

import bagel.*;
import game.GameState;

import java.util.Properties;

/**
 * The title/home screen of the game.
 * Displays the game title and a prompt to start the game.
 */
public class TitleScreen extends Screen {
    private final String TITLE;
    private final String START_PROMPT;
    private final Font titleFont;
    private final Font promptFont;
    private final int titleX;
    private final int titleY;
    private final int promptY;
    private final int windowWidth;
    
    // Background image
    private final Image backgroundImage;

    /**
     * Creates a new title screen.
     *
     * @param gameProps Properties containing game configuration
     * @param messageProps Properties containing game messages
     */
    public TitleScreen(Properties gameProps, Properties messageProps) {
        super(gameProps, messageProps);
        
        TITLE = messageProps.getProperty("home.title");
        START_PROMPT = messageProps.getProperty("home.prompt");
        
        titleFont = loadFont("home.title.fontSize");
        promptFont = loadFont("home.prompt.fontSize");
        
        titleY = Integer.parseInt(gameProps.getProperty("home.title.y"));
        promptY = Integer.parseInt(gameProps.getProperty("home.prompt.y"));
        
        windowWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        titleX = windowWidth / 2;
        
        // Load background image
        backgroundImage = new Image(gameProps.getProperty("backgroundImage"));
    }

    /**
     * Updates the title screen and checks for user input.
     *
     * @param input Current input state
     * @return PLAYING if ENTER is pressed, otherwise null (stay on title screen)
     */
    @Override
    public GameState update(Input input) {
        // Change to gameplay screen when ENTER is pressed for level 1
        if (input.wasPressed(Keys.ENTER)) {
            return GameState.PLAYING;
        }
        
        // For now we don't need to handle level 2 yet
        // This will be implemented in project 2
        
        // Stay on title screen
        return null;
    }

    /**
     * Draws the title screen.
     * Shows the game title and the prompt to start.
     */
    @Override
    public void draw() {
        // Draw background using parent class method
        drawBackground();
        
        // Draw the title centered horizontally
        double titleWidth = titleFont.getWidth(TITLE);
        titleFont.drawString(TITLE, titleX - titleWidth / 2, titleY);
        
        // Draw the prompt centered horizontally
        double promptWidth = promptFont.getWidth(START_PROMPT);
        promptFont.drawString(START_PROMPT, titleX - promptWidth / 2, promptY);
    }
} 