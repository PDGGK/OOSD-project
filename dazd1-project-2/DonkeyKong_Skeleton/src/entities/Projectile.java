package entities;

import bagel.Image;
import bagel.Window;
import interfaces.Collidable;
import interfaces.Movable;

/**
 * Abstract base class for all projectiles in the game.
 * Defines common behavior for bullets and bananas.
 */
public abstract class Projectile extends Entity implements Movable {
    // Physics and movement constants
    private static final double MAX_DISTANCE = 300.0;
    
    // Projectile state
    private double horizontalVelocity = 0;
    private double verticalVelocity = 0;
    private double distanceTraveled = 0;
    private boolean facingRight;
    private boolean active = true;
    
    /**
     * Creates a new projectile at the specified position.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     * @param facingRight Direction the projectile is facing
     * @param image The projectile image
     */
    public Projectile(double x, double y, boolean facingRight, Image image) {
        super(x, y, image);
        this.facingRight = facingRight;
    }
    
    /**
     * Updates the projectile's position based on velocity.
     * Also tracks distance traveled and checks for boundaries.
     */
    @Override
    public void update() {
        if (!active) {
            return;
        }
        
        // Move the projectile
        move();
        
        // Check if projectile has traveled maximum distance
        if (distanceTraveled >= MAX_DISTANCE) {
            deactivate();
        }
        
        // Check if projectile is out of screen bounds
        if (isOutOfBounds()) {
            deactivate();
        }
    }
    
    /**
     * Moves the projectile based on velocity.
     */
    @Override
    public void move() {
        setX(getX() + horizontalVelocity);
        setY(getY() + verticalVelocity);
        
        // Update distance traveled
        distanceTraveled += Math.abs(horizontalVelocity);
    }
    
    /**
     * Sets the projectile's velocity components.
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
     * Gets the projectile's horizontal velocity.
     *
     * @return Current horizontal velocity
     */
    @Override
    public double getXVelocity() {
        return horizontalVelocity;
    }
    
    /**
     * Gets the projectile's vertical velocity.
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
     * @param other The entity this projectile has collided with
     */
    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Platform) {
            // Deactivate when hitting a platform
            deactivate();
        }
    }
    
    /**
     * Checks if the projectile is out of screen bounds.
     *
     * @return true if out of bounds, false otherwise
     */
    private boolean isOutOfBounds() {
        return getX() < 0 || getX() > Window.getWidth() || getY() < 0 || getY() > Window.getHeight();
    }
    
    /**
     * Deactivates the projectile (when it hits something or reaches max distance).
     */
    public void deactivate() {
        active = false;
    }
    
    /**
     * Checks if the projectile is still active.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Gets the maximum travel distance for projectiles.
     *
     * @return Maximum distance in pixels
     */
    public static double getMaxDistance() {
        return MAX_DISTANCE;
    }
    
    /**
     * Checks if the projectile is facing right.
     *
     * @return true if facing right, false otherwise
     */
    public boolean isFacingRight() {
        return facingRight;
    }
    
    /**
     * Sets the horizontal velocity.
     *
     * @param horizontalVelocity The new horizontal velocity
     */
    public void setHorizontalVelocity(double horizontalVelocity) {
        this.horizontalVelocity = horizontalVelocity;
    }
    
    /**
     * Sets the vertical velocity.
     *
     * @param verticalVelocity The new vertical velocity
     */
    public void setVerticalVelocity(double verticalVelocity) {
        this.verticalVelocity = verticalVelocity;
    }
    
    /**
     * Gets the distance the projectile has traveled.
     *
     * @return Distance traveled in pixels
     */
    public double getDistanceTraveled() {
        return distanceTraveled;
    }
    
    /**
     * Overrides the draw method to not render inactive projectiles.
     */
    @Override
    public void draw() {
        if (active) {
            super.draw();
        }
    }
} 