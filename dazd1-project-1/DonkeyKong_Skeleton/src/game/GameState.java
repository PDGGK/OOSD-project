package game;

/**
 * Enumeration of possible game states.
 * Controls the flow of the game and determines which screen is currently active.
 */
public enum GameState {
    /**
     * Title/home screen state. Shown when the game first starts.
     */
    TITLE,
    
    /**
     * Active gameplay state. The main state where the player controls Mario.
     */
    PLAYING,
    
    /**
     * Game over state when the player has won (reached Donkey Kong with a hammer).
     */
    GAME_OVER_WIN,
    
    /**
     * Game over state when the player has lost (touched a barrel without a hammer,
     * touched Donkey Kong without a hammer, or ran out of time).
     */
    GAME_OVER_LOSE
} 