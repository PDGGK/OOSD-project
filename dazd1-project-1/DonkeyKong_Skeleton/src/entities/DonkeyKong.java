package entities;

import bagel.Image;

/**
 * Represents Donkey Kong in the game.
 * Donkey Kong is stationary and placed at the top of the level.
 * Player must reach Donkey Kong with a hammer to win the game.
 */
public class DonkeyKong extends Entity {
    // DonkeyKong image
    private static final Image DONKEY_KONG_IMAGE = new Image("res/donkey_kong.png");
    
    // Physics constants
    private static final double GRAVITY = 0.2;
    private static final double TERMINAL_VELOCITY = 5.0;
    
    // DonkeyKong state
    private double verticalVelocity = 0;
    private boolean onGround = false;

    /**
     * Creates a new Donkey Kong at the specified position.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     */
    public DonkeyKong(double x, double y) {
        super(x, y, DONKEY_KONG_IMAGE);
    }

    /**
     * Updates the Donkey Kong's state, applying gravity if not on ground.
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
     * Sets whether Donkey Kong is on the ground.
     *
     * @param onGround true if on a platform, false otherwise
     */
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    /**
     * Checks if Donkey Kong is on the ground.
     *
     * @return true if on the ground, false otherwise
     */
    public boolean isOnGround() {
        return onGround;
    }

    /**
     * Sets the vertical velocity of Donkey Kong.
     *
     * @param verticalVelocity The new vertical velocity
     */
    public void setVerticalVelocity(double verticalVelocity) {
        this.verticalVelocity = verticalVelocity;
    }
    
    /**
     * Gets the vertical velocity of Donkey Kong.
     *
     * @return The current vertical velocity
     */
    public double getVerticalVelocity() {
        return verticalVelocity;
    }
} 