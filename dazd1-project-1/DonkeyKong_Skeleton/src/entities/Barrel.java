package entities;

import bagel.Image;

/**
 * Represents a barrel in the game.
 * Barrels are stationary obstacles that the player must avoid or jump over.
 * If player has a hammer, they can destroy barrels for points.
 */
public class Barrel extends Entity {
    // Barrel image
    private static final Image BARREL_IMAGE = new Image("res/barrel.png");
    
    // Physics constants
    private static final double GRAVITY = 0.2;
    private static final double TERMINAL_VELOCITY = 5.0;
    
    // Barrel state
    private double verticalVelocity = 0;
    private boolean onGround = false;
    private boolean destroyed = false;

    /**
     * Creates a new barrel at the specified position.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     */
    public Barrel(double x, double y) {
        super(x, y, BARREL_IMAGE);
    }

    /**
     * Updates the barrel's state, applying gravity if not on ground.
     */
    @Override
    public void update() {
        if (!onGround) {
            // Apply gravity
            verticalVelocity = Math.min(verticalVelocity + GRAVITY, TERMINAL_VELOCITY);
            y += verticalVelocity;
        } else {
            // Reset vertical velocity when on ground
            verticalVelocity = 0;
        }
    }

    /**
     * Sets whether the barrel is on the ground.
     *
     * @param onGround true if on a platform, false otherwise
     */
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    /**
     * Checks if the barrel is on the ground.
     *
     * @return true if on the ground, false otherwise
     */
    public boolean isOnGround() {
        return onGround;
    }

    /**
     * Gets the vertical velocity of the barrel.
     *
     * @return The current vertical velocity
     */
    public double getVerticalVelocity() {
        return verticalVelocity;
    }

    /**
     * Sets the vertical velocity of the barrel.
     *
     * @param verticalVelocity The new vertical velocity
     */
    public void setVerticalVelocity(double verticalVelocity) {
        this.verticalVelocity = verticalVelocity;
    }
    
    /**
     * Marks the barrel as destroyed.
     * Used when player with hammer destroys the barrel.
     */
    public void destroy() {
        this.destroyed = true;
    }
    
    /**
     * Checks if the barrel has been destroyed.
     *
     * @return true if destroyed, false otherwise
     */
    public boolean isDestroyed() {
        return destroyed;
    }
    
    /**
     * Overrides the draw method to not draw destroyed barrels.
     */
    @Override
    public void draw() {
        if (!destroyed) {
            super.draw();
        }
    }
} 