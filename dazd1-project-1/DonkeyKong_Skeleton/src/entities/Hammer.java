package entities;

import bagel.Image;

/**
 * Represents the hammer power-up in the game.
 * When collected by the player, it grants invincibility and the ability to destroy barrels.
 */
public class Hammer extends Entity {
    private static final Image HAMMER_IMAGE = new Image("res/hammer.png");
    private boolean collected = false;

    /**
     * Creates a new hammer at the specified position.
     *
     * @param x The x-coordinate of the hammer's center
     * @param y The y-coordinate of the hammer's center
     */
    public Hammer(double x, double y) {
        super(x, y, HAMMER_IMAGE);
    }

    /**
     * Updates the hammer's state.
     * Currently, the hammer doesn't need any update logic as it's stationary.
     */
    @Override
    public void update() {
        // Hammer doesn't need update logic as it's stationary
    }

    /**
     * Checks if the hammer has been collected by the player.
     *
     * @return true if the hammer has been collected, false otherwise
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Marks the hammer as collected.
     */
    public void collect() {
        collected = true;
    }

    /**
     * Draws the hammer on the screen if it hasn't been collected.
     */
    @Override
    public void draw() {
        if (!collected) {
            super.draw();
        }
    }
} 