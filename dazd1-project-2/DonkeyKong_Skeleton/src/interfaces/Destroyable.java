package interfaces;

/**
 * Interface for entities that can be destroyed.
 * Defines destruction behavior and scoring.
 */
public interface Destroyable {
    /**
     * Destroys the entity.
     */
    void destroy();
    
    /**
     * Checks if the entity has been destroyed.
     *
     * @return true if destroyed, false otherwise
     */
    boolean isDestroyed();
    
    /**
     * Gets the score value awarded when this entity is destroyed.
     *
     * @return Score value
     */
    int getScoreValue();
} 