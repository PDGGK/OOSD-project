package entities;

import bagel.Image;
import interfaces.Collidable;
import interfaces.Weapon;

/**
 * Represents a blaster that can be collected by the player.
 * Blasters allow the player to shoot bullets at monkeys and Donkey Kong.
 */
public class Blaster extends Entity implements Weapon {
    // Blaster image
    private static final Image BLASTER_IMAGE = new Image("res/blaster.png");
    
    // Blaster properties
    private static final int INITIAL_BULLET_COUNT = 5;
    
    // Blaster state
    private boolean collected = false;
    
    /**
     * Creates a new blaster at the specified position.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     */
    public Blaster(double x, double y) {
        super(x, y, BLASTER_IMAGE);
    }
    
    /**
     * Updates the blaster's state.
     * Blasters don't move or change after being placed.
     */
    @Override
    public void update() {
        // No update logic needed for stationary blasters
    }
    
    /**
     * Draws the blaster if it hasn't been collected.
     */
    @Override
    public void draw() {
        if (!collected) {
            super.draw();
        }
    }
    
    /**
     * Handles collisions with other entities.
     *
     * @param other The entity this blaster has collided with
     */
    @Override
    public void handleCollision(Collidable other) {
        // Collection is handled in the gameplay screen
    }
    
    /**
     * Marks the blaster as collected by the player.
     * Implements Weapon interface.
     */
    @Override
    public void collect() {
        collected = true;
    }
    
    /**
     * Uses the blaster to fire a bullet.
     * This is handled by the Player class, not here.
     * Implements Weapon interface.
     */
    @Override
    public void use() {
        // Actual shooting logic is in Player class
    }
    
    /**
     * Checks if the blaster is currently active.
     * For blasters, this is the same as being collected.
     * Implements Weapon interface.
     *
     * @return true if the blaster is active, false otherwise
     */
    @Override
    public boolean isActive() {
        return collected;
    }
    
    /**
     * Checks if the blaster has been collected.
     * Implements Weapon interface.
     *
     * @return true if collected, false otherwise
     */
    @Override
    public boolean isCollected() {
        return collected;
    }
    
    /**
     * Gets the initial number of bullets in the blaster.
     *
     * @return Initial bullet count (5)
     */
    public int getInitialBulletCount() {
        return INITIAL_BULLET_COUNT;
    }
} 