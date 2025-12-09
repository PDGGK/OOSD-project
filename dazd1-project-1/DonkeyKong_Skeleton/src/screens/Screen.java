package screens;

import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.Font;
import game.GameState;

import java.util.Properties;

/**
 * Abstract base class for all game screens.
 * Defines common functionality and operations that each screen implements.
 */
public abstract class Screen {
    // Common properties for all screens
    protected final Image backgroundImage;
    protected final Properties gameProps;
    protected final Properties messageProps;
    
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
     * Draws the background image centered on the screen.
     * This is common functionality for all screens.
     */
    protected void drawBackground() {
        backgroundImage.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
    }
    
    /**
     * Helper method to load a font from properties.
     * 
     * @param fontSizeProperty Property name for font size
     * @return The loaded font
     */
    protected Font loadFont(String fontSizeProperty) {
        String fontPath = gameProps.getProperty("font");
        int fontSize = Integer.parseInt(gameProps.getProperty(fontSizeProperty));
        return new Font(fontPath, fontSize);
    }
    
    /**
     * Updates the screen based on user input.
     * This method handles any input processing and state updates.
     *
     * @param input Current input state
     * @return The next GameState, or null if no state change is needed
     */
    public abstract GameState update(Input input);
    
    /**
     * Draws the screen contents.
     * This method renders all visual elements of the screen.
     */
    public abstract void draw();
} 