package interfaces;

/**
 * Interface for entities that can move.
 * Defines standard movement behaviors for game entities.
 */
public interface Movable {
    /**
     * Updates the entity's position based on its velocity.
     */
    void move();
    
    /**
     * Sets the entity's velocity components.
     *
     * @param xVelocity Horizontal velocity component
     * @param yVelocity Vertical velocity component
     */
    void setVelocity(double xVelocity, double yVelocity);
    
    /**
     * Gets the entity's horizontal velocity.
     *
     * @return Current horizontal velocity
     */
    double getXVelocity();
    
    /**
     * Gets the entity's vertical velocity.
     *
     * @return Current vertical velocity
     */
    double getYVelocity();
} 