package entities;

import bagel.Image;
import interfaces.Collidable;
import interfaces.Destroyable;
import interfaces.Movable;

/**
 * Represents a barrel obstacle in the game.
 * Barrels are affected by gravity and can be destroyed by a hammer.
 */
public class Barrel extends Entity implements Movable, Destroyable {
    // Barrel image
    private static final Image BARREL_IMAGE = new Image("res/barrel.png");
    
    // Physics constants for barrels
    private static final double INITIAL_DOWNWARD_VELOCITY = 0.4; // Initial downward velocity from section 2.3.2
    private static final double GRAVITY = 0.2; // Gravity acceleration from section 4
    private static final double TERMINAL_VELOCITY = 5.0;
    
    // Scoring constants
    private static final int SCORE_VALUE = 100;
    
    // Barrel state
    private double verticalVelocity = INITIAL_DOWNWARD_VELOCITY; // Start with initial velocity
    private double horizontalVelocity = 0;
    private boolean onGround = false;
    private boolean destroyed = false;

    /**
     * Creates a new Barrel at the specified position.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     */
    public Barrel(double x, double y) {
        super(x, y, BARREL_IMAGE);
    }

    /**
     * Updates the barrel's state.
     * Applies gravity if not on ground.
     */
    @Override
    public void update() {
        if (destroyed) {
            return;
        }
        
        move();
    }
    
    /**
     * Moves the barrel based on current velocity and state.
     * Applies gravity if not on ground.
     */
    @Override
    public void move() {
        if (!onGround) {
            // Apply gravity
            verticalVelocity = Math.min(verticalVelocity + GRAVITY, TERMINAL_VELOCITY);
            setY(getY() + verticalVelocity);
        } else {
            // Reset vertical velocity when on ground
            verticalVelocity = 0;
        }
        
        // Apply horizontal movement
        setX(getX() + horizontalVelocity);
    }
    
    /**
     * Sets the barrel's velocity components.
     *
     * @param xVelocity Horizontal velocity component
     * @param yVelocity Vertical velocity component
     */
    @Override
    public void setVelocity(double xVelocity, double yVelocity) {
        this.horizontalVelocity = xVelocity;
        this.verticalVelocity = yVelocity;
    }
    
    /**
     * Gets the barrel's horizontal velocity.
     *
     * @return Current horizontal velocity
     */
    @Override
    public double getXVelocity() {
        return horizontalVelocity;
    }
    
    /**
     * Gets the barrel's vertical velocity.
     *
     * @return Current vertical velocity
     */
    @Override
    public double getYVelocity() {
        return verticalVelocity;
    }
    
    /**
     * Handles collisions with other entities.
     *
     * @param other The entity this barrel has collided with
     */
    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Platform) {
            // Handle platform collision
            handlePlatformCollision((Platform) other);
        }
    }
    
    /**
     * Handles collision with a platform.
     *
     * @param platform The platform collided with
     */
    private void handlePlatformCollision(Platform platform) {
        // Land on platform
        if (verticalVelocity > 0) {
            platform.placeEntityOnTop(this);
            setOnGround(true);
        }
    }

    /**
     * Marks the barrel as destroyed.
     * Implements the Destroyable interface.
     */
    @Override
    public void destroy() {
        destroyed = true;
    }
    
    /**
     * Checks if the barrel has been destroyed.
     * Implements the Destroyable interface.
     *
     * @return true if destroyed, false otherwise
     */
    @Override
    public boolean isDestroyed() {
        return destroyed;
    }
    
    /**
     * Gets the score value for destroying this barrel.
     * Implements the Destroyable interface.
     *
     * @return The score value (100 points)
     */
    @Override
    public int getScoreValue() {
        return SCORE_VALUE;
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
} 