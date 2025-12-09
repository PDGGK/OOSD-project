package entities;

import bagel.Image;
import interfaces.Collidable;

/**
 * Represents a bullet fired by the player with a blaster.
 * Bullets can damage Donkey Kong and destroy monkeys.
 */
public class Bullet extends Projectile {
    // Bullet images
    private static final Image BULLET_RIGHT_IMAGE = new Image("res/bullet_right.png");
    private static final Image BULLET_LEFT_IMAGE = new Image("res/bullet_left.png");
    
    // Bullet physics
    private static final double BULLET_SPEED = 3.8;
    
    /**
     * Creates a new bullet at the specified position.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     * @param facingRight Direction the bullet is fired (true for right, false for left)
     */
    public Bullet(double x, double y, boolean facingRight) {
        super(x, y, facingRight, facingRight ? BULLET_RIGHT_IMAGE : BULLET_LEFT_IMAGE);
        
        // Set initial velocity based on direction
        setHorizontalVelocity(facingRight ? BULLET_SPEED : -BULLET_SPEED);
        setVerticalVelocity(0); // Bullets move only horizontally
    }
    
    /**
     * Handles collisions with other entities.
     * Bullets can damage Donkey Kong and destroy monkeys.
     *
     * @param other The entity this bullet has collided with
     */
    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Platform) {
            // Deactivate when hitting a platform
            deactivate();
        } else if (other instanceof DonkeyKong) {
            // Damage Donkey Kong
            DonkeyKong donkeyKong = (DonkeyKong) other;
            donkeyKong.takeDamage(1);
            deactivate();
        } else if (other instanceof Monkey) {
            // Destroy monkey
            Monkey monkey = (Monkey) other;
            if (!monkey.isDestroyed()) {
                monkey.destroy();
                deactivate();
            }
        }
    }
    
    /**
     * Gets the bullet's damage value.
     * Used when hitting Donkey Kong.
     *
     * @return Damage value (1 point)
     */
    public int getDamage() {
        return 1;
    }
} 