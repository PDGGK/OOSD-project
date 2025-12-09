package entities;

import bagel.Image;
import interfaces.Collidable;
import interfaces.Weapon;

/**
 * Represents the hammer power-up in the game.
 * When collected by the player, it grants invincibility and the ability to destroy barrels.
 */
public class Hammer extends Entity implements Weapon {
    private static final Image HAMMER_IMAGE = new Image("res/hammer.png");
    private boolean collected = false;
    private boolean active = false;

    /**
     * Creates a new hammer at the specified position.
     *
     * @param x The x-coordinate of the hammer's center
     * @param y The y-coordinate of the hammer's center
     */
    public Hammer(double x, double y) {
        super(x, y, HAMMER_IMAGE);
    }

    /**
     * Updates the hammer's state.
     * Currently, the hammer doesn't need any update logic as it's stationary.
     */
    @Override
    public void update() {
        // Hammer doesn't need update logic as it's stationary
    }
    
    /**
     * Handles the effects of a collision with another entity.
     *
     * @param other The entity this object has collided with
     */
    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Player && !collected) {
            // Hammer collection is handled in the gameplay logic
        }
    }

    /**
     * Checks if the hammer has been collected by the player.
     *
     * @return true if the hammer has been collected, false otherwise
     */
    @Override
    public boolean isCollected() {
        return collected;
    }

    /**
     * Marks the hammer as collected.
     */
    @Override
    public void collect() {
        collected = true;
        active = true;
    }
    
    /**
     * Uses the hammer's ability.
     * For the hammer, this method doesn't need to do anything specific
     * as the hammer is automatically used when collected.
     */
    @Override
    public void use() {
        // Hammer is automatically used when collected
        // This method is included to fulfill the Weapon interface
    }
    
    /**
     * Checks if the hammer is currently active.
     *
     * @return true if the hammer is active, false otherwise
     */
    @Override
    public boolean isActive() {
        return active;
    }
    
    /**
     * Sets whether the hammer is active.
     * This would be used if the hammer has a time limit.
     *
     * @param active true to activate the hammer, false to deactivate
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Draws the hammer on the screen if it hasn't been collected.
     */
    @Override
    public void draw() {
        if (!collected) {
            super.draw();
        }
    }
} 