package entities;

import bagel.Image;
import interfaces.Collidable;
import interfaces.Movable;

/**
 * Represents Donkey Kong in the game.
 * Donkey Kong is stationary and placed at the top of the level.
 * Player must reach Donkey Kong with a hammer to win the game.
 * In level 2, player can also defeat Donkey Kong by shooting him 5 times with a blaster.
 */
public class DonkeyKong extends Entity implements Movable {
    // DonkeyKong image
    private static final Image DONKEY_KONG_IMAGE = new Image("res/donkey_kong.png");
    
    // Physics constants
    private static final double GRAVITY = 0.4;
    private static final double TERMINAL_VELOCITY = 5.0;
    
    // Health system constants
    private static final int MAX_HEALTH = 5;
    
    // DonkeyKong state
    private double verticalVelocity = 0;
    private double horizontalVelocity = 0;
    private boolean onGround = false;
    private int health;

    /**
     * Creates a new Donkey Kong at the specified position.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     */
    public DonkeyKong(double x, double y) {
        super(x, y, DONKEY_KONG_IMAGE);
        this.health = MAX_HEALTH; // Initialize with max health
    }

    /**
     * Updates the Donkey Kong's state.
     * Delegates to move method.
     */
    @Override
    public void update() {
        move();
    }
    
    /**
     * Moves Donkey Kong based on current velocity and state.
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
        
        // Apply horizontal velocity
        setX(getX() + horizontalVelocity);
    }
    
    /**
     * Sets Donkey Kong's velocity components.
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
     * Gets Donkey Kong's horizontal velocity.
     *
     * @return Current horizontal velocity
     */
    @Override
    public double getXVelocity() {
        return horizontalVelocity;
    }
    
    /**
     * Gets Donkey Kong's vertical velocity.
     *
     * @return Current vertical velocity
     */
    @Override
    public double getYVelocity() {
        return verticalVelocity;
    }
    
    /**
     * Handles the effects of a collision with another entity.
     *
     * @param other The entity this object has collided with
     */
    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Platform) {
            // Handle platform collision
            handlePlatformCollision((Platform) other);
        } else if (other instanceof Player) {
            // Player collision is handled in the gameplay logic
        }
    }
    
    /**
     * Handles collision with a platform.
     * Places Donkey Kong on top of the platform.
     *
     * @param platform The platform collided with
     */
    private void handlePlatformCollision(Platform platform) {
        // Only handle landing on platforms
        if (verticalVelocity > 0) {
            // Land on platform
            setY(platform.getY() - (platform.getHeight() / 2) - (getHeight() / 2));
            setOnGround(true);
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
    
    /**
     * Gets Donkey Kong's current health.
     * 
     * @return Current health value (1-5)
     */
    public int getHealth() {
        return health;
    }
    
    /**
     * Sets Donkey Kong's health to a specific value.
     * 
     * @param health The new health value (clamped between 0 and MAX_HEALTH)
     */
    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, MAX_HEALTH));
    }
    
    /**
     * Damages Donkey Kong, reducing health by the specified amount.
     * 
     * @param damage Amount of damage to inflict
     * @return true if the damage caused Donkey Kong to die, false otherwise
     */
    public boolean takeDamage(int damage) {
        health = Math.max(0, health - damage);
        return isDead();
    }
    
    /**
     * Instantly defeats Donkey Kong by setting health to 0.
     * Used when player touches Donkey Kong with a hammer.
     */
    public void defeat() {
        health = 0;
    }
    
    /**
     * Checks if Donkey Kong is defeated (health is 0).
     * 
     * @return true if Donkey Kong is defeated, false otherwise
     */
    public boolean isDead() {
        return health <= 0;
    }
    
    /**
     * Gets the maximum possible health for Donkey Kong.
     * 
     * @return Maximum health value
     */
    public static int getMaxHealth() {
        return MAX_HEALTH;
    }
} 