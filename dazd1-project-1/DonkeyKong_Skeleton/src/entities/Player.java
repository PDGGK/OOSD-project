package entities;

import bagel.Image;
import bagel.Input;
import bagel.Keys;

/**
 * Represents the player character (Mario) in the game.
 * Handles player movement, jumping, and interaction with other entities.
 */
public class Player extends Entity {
    // Image resources for player in different states
    private static final Image RIGHT_IMAGE = new Image("res/mario_right.png");
    private static final Image LEFT_IMAGE = new Image("res/mario_left.png");
    private static final Image HAMMER_RIGHT_IMAGE = new Image("res/mario_hammer_right.png");
    private static final Image HAMMER_LEFT_IMAGE = new Image("res/mario_hammer_left.png");
    
    // Movement constants
    private static final double MOVE_SPEED = 3.5;
    private static final double CLIMB_SPEED = 2.0;
    
    // Physics constants
    private static final double GRAVITY = 0.2;
    private static final double TERMINAL_VELOCITY = 10;
    
    // Player state
    private boolean facingRight = true;
    private boolean onGround = false;
    private boolean onLadder = false;
    private double verticalVelocity = 0;
    private boolean hasHammer = false;
    
    // Reference to current ladder (if any)
    private Ladder currentLadder = null;
    
    // Previous position tracking (for collision resolution)
    private double previousY;

    /**
     * Creates a new player at the specified position.
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public Player(double x, double y) {
        super(x, y, RIGHT_IMAGE); // Default to facing right
        this.previousY = y;
    }

    /**
     * Updates the player's state, applying gravity if not on ground or ladder.
     * Also manages vertical velocity and position.
     */
    @Override
    public void update() {
        // Save previous position for collision resolution
        previousY = y;
        
        // Only apply gravity if not on ground and not on ladder
        if (!onGround && !onLadder) {
            // Apply gravity
            verticalVelocity = Math.min(verticalVelocity + GRAVITY, TERMINAL_VELOCITY);
            y += verticalVelocity;
        } else if (onGround) {
            // Reset vertical velocity when on ground
            verticalVelocity = 0;
        }
        // When on ladder, vertical movement is controlled by input
    }
    
    /**
     * Handles player input for movement.
     * Updates player images based on direction and hammer state.
     *
     * @param input The current input state
     * @param windowWidth The width of the game window for boundary checking
     * @param windowHeight The height of the game window for boundary checking
     */
    public void handleInput(Input input, int windowWidth, int windowHeight) {
        // Handle climbing when on ladder
        if (onLadder && currentLadder != null) {
            // Vertical movement on ladder - fixed speed of 2 pixels per frame
            if (input.isDown(Keys.UP)) {
                y -= CLIMB_SPEED;
            }
            
            if (input.isDown(Keys.DOWN)) {
                y += CLIMB_SPEED;
            }
            
            // Horizontal movement on ladder - standard speed (no reduction)
            if (input.isDown(Keys.LEFT)) {
                if (x > image.getWidth()/2) {
                    x -= MOVE_SPEED; // Standard movement speed
                }
                
                if (facingRight) {
                    facingRight = false;
                    // Update image based on hammer state
                    image = hasHammer ? HAMMER_LEFT_IMAGE : LEFT_IMAGE;
                }
            }
            
            if (input.isDown(Keys.RIGHT)) {
                if (x < windowWidth - image.getWidth()/2) {
                    x += MOVE_SPEED; // Standard movement speed
                }
                
                if (!facingRight) {
                    facingRight = true;
                    // Update image based on hammer state
                    image = hasHammer ? HAMMER_RIGHT_IMAGE : RIGHT_IMAGE;
                }
            }
            
            // No jumping allowed when on ladder
            return;
        }
        
        // Normal movement when not on ladder
        // Handle left movement
        if (input.isDown(Keys.LEFT)) {
            if (x > image.getWidth()/2) { // Boundary check with center-based positioning
                x -= MOVE_SPEED;
            }
            
            // Update image direction if needed
            if (facingRight) {
                facingRight = false;
                // Update image based on hammer state
                image = hasHammer ? HAMMER_LEFT_IMAGE : LEFT_IMAGE;
            }
        }
        
        // Handle right movement
        if (input.isDown(Keys.RIGHT)) {
            if (x < windowWidth - image.getWidth()/2) { // Boundary check with center-based positioning
                x += MOVE_SPEED;
            }
            
            // Update image direction if needed
            if (!facingRight) {
                facingRight = true;
                // Update image based on hammer state
                image = hasHammer ? HAMMER_RIGHT_IMAGE : RIGHT_IMAGE;
            }
        }
        
        // Handle jumping - only allow jumping when on the ground
        if (input.wasPressed(Keys.SPACE) && onGround) {
            // Apply initial upward velocity (negative is up)
            verticalVelocity = -5;
            // Player is no longer on the ground
            onGround = false;
        }
    }
    
    /**
     * Starts climbing a ladder.
     * Enters climbing state and references the current ladder.
     *
     * @param ladder The ladder to climb
     */
    public void startClimbing(Ladder ladder) {
        onLadder = true;
        currentLadder = ladder;
        
        // Stop any vertical movement
        verticalVelocity = 0;
    }
    
    /**
     * Stops climbing a ladder.
     * Resets ladder state and allows normal movement.
     */
    public void stopClimbing() {
        onLadder = false;
        currentLadder = null;
    }
    
    /**
     * Sets whether the player is on the ground.
     * This is used by the platform collision detection.
     *
     * @param onGround true if the player is on a platform, false otherwise
     */
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
    
    /**
     * Checks if the player is on the ground.
     *
     * @return true if the player is on the ground, false otherwise
     */
    public boolean isOnGround() {
        return onGround;
    }
    
    /**
     * Checks if the player is on a ladder.
     *
     * @return true if the player is on a ladder, false otherwise
     */
    public boolean isOnLadder() {
        return onLadder;
    }
    
    /**
     * Gets the current ladder the player is climbing.
     *
     * @return The current ladder, or null if not climbing
     */
    public Ladder getCurrentLadder() {
        return currentLadder;
    }
    
    /**
     * Gets the vertical velocity of the player.
     *
     * @return The current vertical velocity
     */
    public double getVerticalVelocity() {
        return verticalVelocity;
    }
    
    /**
     * Sets the vertical velocity of the player.
     *
     * @param verticalVelocity The new vertical velocity
     */
    public void setVerticalVelocity(double verticalVelocity) {
        this.verticalVelocity = verticalVelocity;
    }
    
    /**
     * Gets the player's previous Y position.
     * Used for collision resolution to prevent platform penetration.
     *
     * @return The previous Y coordinate
     */
    public double getPreviousY() {
        return previousY;
    }
    
    /**
     * Checks if the player has a hammer.
     * This will be used in step 13 when implementing hammer functionality.
     *
     * @return true if the player has a hammer, false otherwise
     */
    public boolean hasHammer() {
        return hasHammer;
    }
    
    /**
     * Collects a hammer, granting the player the ability to destroy barrels and defeat Donkey Kong.
     * Updates the player's image to show they are carrying a hammer.
     */
    public void collectHammer() {
        hasHammer = true;
        
        // Update player image to show hammer
        if (facingRight) {
            image = HAMMER_RIGHT_IMAGE;
        } else {
            image = HAMMER_LEFT_IMAGE;
        }
    }
} 