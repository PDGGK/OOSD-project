package interfaces;

import bagel.util.Rectangle;

/**
 * Interface for entities that can collide with other entities.
 * Provides standard collision detection and handling.
 */
public interface Collidable {
    /**
     * Gets the bounding box for collision detection.
     *
     * @return Rectangle representing the entity's collision area
     */
    Rectangle getBoundingBox();
    
    /**
     * Checks if this entity collides with another collidable entity.
     *
     * @param other The other entity to check collision with
     * @return true if collision detected, false otherwise
     */
    default boolean collidesWith(Collidable other) {
        return getBoundingBox().intersects(other.getBoundingBox());
    }
    
    /**
     * Handles the effects of a collision with another entity.
     *
     * @param other The entity this object has collided with
     */
    void handleCollision(Collidable other);
} 