package screens;

import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.Font;
import game.GameState;

import java.util.Properties;

/**
 * Abstract base class for all game screens.
 * Provides common functionality and defines the interface for screen management.
 */
public abstract class Screen {
    private final Image backgroundImage;
    private final Properties gameProps;
    private final Properties messageProps;
    
    /**
     * Creates a new screen with the specified properties.
     *
     * @param gameProps Properties containing game configuration
     * @param messageProps Properties containing game messages
     */
    public Screen(Properties gameProps, Properties messageProps) {
        this.gameProps = gameProps;
        this.messageProps = messageProps;
        
        // Load background image - common to all screens
        this.backgroundImage = new Image(gameProps.getProperty("backgroundImage"));
    }
    
    /**
     * Updates the screen state and handles input.
     *
     * @param input The current input state
     * @return The new game state, or null if no state change is requested
     */
    public abstract GameState update(Input input);
    
    /**
     * Renders the screen to the display.
     */
    public abstract void draw();
    
    /**
     * Draws the background image centered on the screen.
     */
    private void drawBackground() {
        // Draw background centered on the screen
        double backgroundX = Window.getWidth() / 2.0;
        double backgroundY = Window.getHeight() / 2.0;
        backgroundImage.draw(backgroundX, backgroundY);
    }
    
    /**
     * Creates a Font object from a font size property.
     *
     * @param fontSizeProperty The property key for the font size
     * @return A Font object with the specified size
     */
    private Font loadFont(String fontSizeProperty) {
        int fontSize = Integer.parseInt(gameProps.getProperty(fontSizeProperty));
        return new Font(gameProps.getProperty("font"), fontSize);
    }
    
    /**
     * Gets the game properties.
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
    
    /**
     * Gets the background image.
     *
     * @return The background image
     */
    public Image getBackgroundImage() {
        return backgroundImage;
    }
    
    /**
     * Draws the background image centered on the screen.
     * Protected method for subclasses to use.
     */
    protected void drawBackgroundImage() {
        drawBackground();
    }
    
    /**
     * Creates a Font object from a font size property.
     * Protected method for subclasses to use.
     *
     * @param fontSizeProperty The property key for the font size
     * @return A Font object with the specified size
     */
    protected Font createFont(String fontSizeProperty) {
        return loadFont(fontSizeProperty);
    }
} 