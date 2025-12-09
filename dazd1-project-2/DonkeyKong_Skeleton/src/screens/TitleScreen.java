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
        
        // Load fonts
        titleFont = createFont("home.title.fontSize");
        promptFont = createFont("home.prompt.fontSize");
        
        // Load positions
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
     * @return LEVEL1 if ENTER is pressed, LEVEL2 if '2' is pressed, otherwise null (stay on title screen)
     */
    @Override
    public GameState update(Input input) {
        // Change to level 1 (gameplay screen) when ENTER is pressed
        if (input.wasPressed(Keys.ENTER)) {
            return GameState.LEVEL1;
        }
        
        // Change directly to level 2 when '2' key is pressed
        if (input.wasPressed(Keys.NUM_2)) {
            return GameState.LEVEL2;
        }
        
        // Stay on title screen
        return null;
    }

    /**
     * Draws the title screen.
     * Shows the game title and the prompt to start.
     */
    @Override
    public void draw() {
        // Draw background
        drawBackgroundImage();
        
        // Draw title (centered horizontally)
        double titleWidth = titleFont.getWidth(TITLE);
        double titleCenterX = windowWidth / 2.0 - titleWidth / 2.0;
        titleFont.drawString(TITLE, titleCenterX, titleY);
        
        // Draw prompt messages (centered horizontally)
        double promptWidth = promptFont.getWidth(START_PROMPT);
        double promptCenterX = windowWidth / 2.0 - promptWidth / 2.0;
        promptFont.drawString(START_PROMPT, promptCenterX, promptY);
    }
} 