package entities;

import bagel.Image;
import interfaces.Collidable;

/**
 * Represents a banana projectile thrown by intelligent monkeys.
 * Bananas move in a straight line and are dangerous to Mario.
 */
public class Banana extends Projectile {
    // Banana image
    private static final Image BANANA_IMAGE = new Image("res/banana.png");
    
    // Banana physics
    private static final double BANANA_SPEED = 1.8;
    
    /**
     * Creates a new banana at the specified position.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     * @param facingRight Direction the banana is thrown (true for right, false for left)
     */
    public Banana(double x, double y, boolean facingRight) {
        super(x, y, facingRight, BANANA_IMAGE);
        
        // Set initial velocity based on direction
        setHorizontalVelocity(facingRight ? BANANA_SPEED : -BANANA_SPEED);
        setVerticalVelocity(0); // Bananas move only horizontally
    }
    
    /**
     * Updates the banana's position.
     */
    @Override
    public void update() {
        super.update(); // Update position and check distance/bounds
    }
    
    /**
     * Handles collisions with other entities.
     * Bananas are not affected by hammers but damage Mario.
     *
     * @param other The entity this banana has collided with
     */
    @Override
    public void handleCollision(Collidable other) {
        // Bananas only interact with Mario (handled in Level2Screen)
        if (other instanceof Platform) {
            // Deactivate when hitting a platform
            deactivate();
        }
    }
} 