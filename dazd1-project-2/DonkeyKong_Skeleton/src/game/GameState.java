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
     * Level 1 gameplay state. The first level where the player controls Mario.
     */
    LEVEL1,
    
    /**
     * Level 2 gameplay state. The second level with additional game elements.
     */
    LEVEL2,
    
    /**
     * Game over state when the player has won (reached Donkey Kong with a hammer 
     * in level 1 or reduced Donkey Kong's health to 0 in level 2).
     */
    GAME_OVER_WIN,
    
    /**
     * Game over state when the player has lost (touched a barrel without a hammer,
     * touched Donkey Kong without a hammer, touched a monkey, hit by a banana,
     * or ran out of time).
     */
    GAME_OVER_LOSE
} 