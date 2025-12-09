package entities;

import bagel.Image;
import bagel.util.Rectangle;
import interfaces.Collidable;

/**
 * Abstract base class for all game entities.
 * Provides common attributes and methods that all entities share.
 */
public abstract class Entity implements Collidable {
    private double x;
    private double y;
    private Image image;

    /**
     * Creates a new entity with the given position and image.
     * The position (x,y) represents the center of the entity.
     *
     * @param x     The x-coordinate of the entity's center
     * @param y     The y-coordinate of the entity's center
     * @param image The image representing the entity
     */
    public Entity(double x, double y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    /**
     * Draws the entity on the screen from its center point.
     */
    public void draw() {
        image.draw(x, y);
    }

    /**
     * Updates the entity's state.
     * Each type of entity should override this method to implement its specific behavior.
     */
    public abstract void update();

    /**
     * Gets the bounding box of the entity for collision detection.
     *
     * @return A Rectangle representing the entity's bounding box
     */
    @Override
    public Rectangle getBoundingBox() {
        // Calculate the top-left corner based on center position
        double topLeftX = x - (image.getWidth() / 2);
        double topLeftY = y - (image.getHeight() / 2);
        return new Rectangle(topLeftX, topLeftY, image.getWidth(), image.getHeight());
    }
    
    /**
     * Checks if this entity collides with another collidable entity.
     *
     * @param other The other entity to check collision with
     * @return true if collision detected, false otherwise
     */
    @Override
    public boolean collidesWith(Collidable other) {
        return this.getBoundingBox().intersects(other.getBoundingBox());
    }
    
    /**
     * Handles the effects of a collision with another entity.
     * Each subclass should implement this method to define its collision behavior.
     *
     * @param other The entity this object has collided with
     */
    @Override
    public abstract void handleCollision(Collidable other);

    /**
     * Gets the entity's center x-coordinate.
     *
     * @return The center x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the entity's center y-coordinate.
     *
     * @return The center y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the entity's width.
     *
     * @return The width
     */
    public double getWidth() {
        return image.getWidth();
    }

    /**
     * Gets the entity's height.
     *
     * @return The height
     */
    public double getHeight() {
        return image.getHeight();
    }
    
    /**
     * Sets the entity's center x-coordinate.
     *
     * @param x The new center x-coordinate
     */
    public void setX(double x) {
        this.x = x;
    }
    
    /**
     * Sets the entity's center y-coordinate.
     *
     * @param y The new center y-coordinate
     */
    public void setY(double y) {
        this.y = y;
    }
    
    /**
     * Gets the entity's image.
     *
     * @return The entity's image
     */
    public Image getImage() {
        return image;
    }
    
    /**
     * Sets the entity's image.
     *
     * @param image The new image for the entity
     */
    public void setImage(Image image) {
        this.image = image;
    }
    
    /**
     * Gets the entity's top left x-coordinate.
     *
     * @return The top left x-coordinate
     */
    public double getTopLeftX() {
        return x - (image.getWidth() / 2);
    }
    
    /**
     * Gets the entity's top left y-coordinate.
     *
     * @return The top left y-coordinate
     */
    public double getTopLeftY() {
        return y - (image.getHeight() / 2);
    }
} 