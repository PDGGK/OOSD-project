package entities;

import bagel.Image;
import interfaces.Collidable;

/**
 * Represents a ladder in the game.
 * Ladders allow the player to climb up and down between platforms.
 */
public class Ladder extends Entity {
    // Ladder image
    private static final Image LADDER_IMAGE = new Image("res/ladder.png");
    
    // Physics constants
    private static final double GRAVITY = 0.25;
    private static final double TERMINAL_VELOCITY = 5.0;
    
    // Player interaction constants
    private static final double HORIZONTAL_ALIGNMENT_MARGIN = 5.0; // Extra horizontal margin for player alignment
    private static final double VERTICAL_ALIGNMENT_TOLERANCE = 10.0; // Tolerance for vertical position detection
    
    // Ladder state
    private double verticalVelocity = 0;
    private boolean onGround = false;

    /**
     * Creates a new ladder at the specified position.
     *
     * @param x The x-coordinate of the ladder's center
     * @param y The y-coordinate of the ladder's center
     */
    public Ladder(double x, double y) {
        super(x, y, LADDER_IMAGE);
        this.verticalVelocity = 0;
        this.onGround = false;
    }

    /**
     * Updates the ladder's state, applying gravity if not on ground.
     */
    @Override
    public void update() {
        if (!onGround) {
            // Apply gravity
            verticalVelocity = Math.min(verticalVelocity + GRAVITY, TERMINAL_VELOCITY);
            setY(getY() + verticalVelocity);
        } else {
            // Reset vertical velocity when on ground
            verticalVelocity = 0;
        }
    }

    /**
     * Checks if the player is positioned such that they can climb this ladder.
     * Player must be horizontally centered within the ladder's width.
     *
     * @param player The player to check
     * @return true if the player can climb this ladder
     */
    public boolean canPlayerClimb(Player player) {
        double playerCenterX = player.getX();
        double ladderLeftEdge = this.getX() - this.getWidth()/2;
        double ladderRightEdge = this.getX() + this.getWidth()/2;
        
        // Check if player is horizontally within ladder bounds
        boolean isWithinHorizontalBounds = playerCenterX >= ladderLeftEdge && 
                                           playerCenterX <= ladderRightEdge;
                                           
        // Check if player is vertically within ladder bounds
        double ladderTopY = this.getY() - this.getHeight()/2;
        double ladderBottomY = this.getY() + this.getHeight()/2;
        double playerY = player.getY();
        
        boolean isWithinVerticalBounds = playerY >= ladderTopY && 
                                         playerY <= ladderBottomY;
        
        return isWithinHorizontalBounds && isWithinVerticalBounds;
    }
    
    /**
     * Checks if the player is positioned at the top of this ladder.
     * This is used to allow players to start climbing down when
     * they are standing on a platform above a ladder.
     * 
     * @param player The player entity to check
     * @return true if player is at the top of this ladder
     */
    public boolean isPlayerAtTopOfLadder(Player player) {
        // Get player's bottom center position
        double playerBottomY = player.getY() + player.getHeight()/2;
        double playerX = player.getX();
        
        // Get ladder's top edge position
        double ladderTopY = this.getY() - this.getHeight()/2;
        
        // Player is considered at top of ladder if:
        // 1. Within horizontal bounds (centered plus small margin)
        // 2. Their bottom edge is close to (slightly above) the ladder's top edge
        boolean isHorizontallyAligned = Math.abs(playerX - this.getX()) < this.getWidth()/2 + 5;
        boolean isAtTopEdge = Math.abs(playerBottomY - ladderTopY) < 10;
        
        return isHorizontallyAligned && isAtTopEdge && player.isOnGround();
    }
    
    /**
     * Checks if the player is positioned at the bottom of this ladder.
     * Used to detect when a player should stop climbing.
     *
     * @param player The player to check
     * @return true if the player is at the bottom of this ladder
     */
    public boolean isPlayerAtBottomOfLadder(Player player) {
        double playerCenterX = player.getX();
        double ladderLeftEdge = this.getX() - this.getWidth()/2;
        double ladderRightEdge = this.getX() + this.getWidth()/2;
        
        // Check if player is horizontally within ladder bounds
        boolean isWithinHorizontalBounds = playerCenterX >= ladderLeftEdge && 
                                           playerCenterX <= ladderRightEdge;
                                           
        // Check if player is at the bottom of the ladder
        double ladderBottomY = this.getY() + this.getHeight()/2;
        double playerTopY = player.getY() - player.getHeight()/2;
        
        // Player top should be near ladder bottom
        boolean isAtBottom = Math.abs(playerTopY - ladderBottomY) < VERTICAL_ALIGNMENT_TOLERANCE;
        
        return isWithinHorizontalBounds && isAtBottom;
    }

    /**
     * Sets whether the ladder is on the ground.
     *
     * @param onGround true if the ladder is on a platform, false otherwise
     */
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    /**
     * Checks if the ladder is on the ground.
     *
     * @return true if the ladder is on the ground, false otherwise
     */
    public boolean isOnGround() {
        return onGround;
    }

    /**
     * Sets the vertical velocity of the ladder.
     *
     * @param verticalVelocity The new vertical velocity
     */
    public void setVerticalVelocity(double verticalVelocity) {
        this.verticalVelocity = verticalVelocity;
    }
    
    /**
     * Gets the vertical velocity of the ladder.
     *
     * @return The current vertical velocity
     */
    public double getVerticalVelocity() {
        return verticalVelocity;
    }

    /**
     * Handles the effects of a collision with another entity.
     * Ladders don't need to respond to collisions themselves,
     * as the entities that collide with them handle the effects.
     *
     * @param other The entity this object has collided with
     */
    @Override
    public void handleCollision(Collidable other) {
        // Ladders don't need to respond to collisions
        // The colliding entities handle the effects
    }
} 