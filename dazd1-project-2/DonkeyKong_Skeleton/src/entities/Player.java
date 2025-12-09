package entities;

import bagel.Image;
import bagel.Input;
import bagel.Keys;
import interfaces.Collidable;
import interfaces.Movable;

/**
 * Represents the player character (Mario) in the game.
 * Handles player movement, jumping, and interaction with other entities.
 */
public class Player extends Entity implements Movable {
    // Player constants
    private static final Image RIGHT_IMAGE = new Image("res/mario_right.png");
    private static final Image LEFT_IMAGE = new Image("res/mario_left.png");
    private static final Image HAMMER_RIGHT_IMAGE = new Image("res/mario_hammer_right.png");
    private static final Image HAMMER_LEFT_IMAGE = new Image("res/mario_hammer_left.png");
    private static final Image BLASTER_RIGHT_IMAGE = new Image("res/mario_blaster_right.png");
    private static final Image BLASTER_LEFT_IMAGE = new Image("res/mario_blaster_left.png");
    
    // Movement constants
    private static final double MOVE_SPEED = 3.5;
    private static final double CLIMB_SPEED = 2.0;
    
    // Physics constants
    private static final double GRAVITY = 0.2;
    private static final double TERMINAL_VELOCITY = 10;
    private static final double JUMP_VELOCITY = -5.0; // Initial upward velocity (negative is up)
    
    // Player state
    private boolean facingRight = true;
    private boolean onGround = false;
    private boolean onLadder = false;
    private double verticalVelocity = 0;
    private double horizontalVelocity = 0;
    private boolean hasHammer = false;
    private boolean hasBlaster = false;
    private int bulletCount = 0;
    
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
     * Updates the player's state.
     * Delegates movement logic to the move method.
     */
    @Override
    public void update() {
        // Save previous position for collision resolution
        previousY = getY();
        
        // Handle movement
        move();
    }
    
    /**
     * Moves the player based on current velocity and state.
     * Applies gravity if not on ground or ladder.
     */
    @Override
    public void move() {
        // Only apply gravity if not on ground and not on ladder
        if (!onGround && !onLadder) {
            // Apply gravity
            verticalVelocity = Math.min(verticalVelocity + GRAVITY, TERMINAL_VELOCITY);
            setY(getY() + verticalVelocity);
        } else if (onGround) {
            // Reset vertical velocity when on ground
            verticalVelocity = 0;
        }
        
        // Apply horizontal velocity
        setX(getX() + horizontalVelocity);
    }
    
    /**
     * Sets the player's velocity components.
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
     * Gets the player's horizontal velocity.
     *
     * @return Current horizontal velocity
     */
    @Override
    public double getXVelocity() {
        return horizontalVelocity;
    }
    
    /**
     * Gets the player's vertical velocity.
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
        // Delegate to appropriate handlers based on the type of the other entity
        if (other instanceof Platform) {
            // Handle platform collision
            handlePlatformCollision((Platform) other);
        } else if (other instanceof Ladder) {
            // Just detect the overlap; actual climbing is handled elsewhere
        } else if (other instanceof Barrel) {
            // Barrel collision might end the game if not carrying hammer
        } else if (other instanceof Hammer) {
            // Hammer collection is handled elsewhere
        } else if (other instanceof DonkeyKong) {
            // DonkeyKong collision might trigger victory if carrying hammer
        }
    }
    
    /**
     * Handles collision with a platform.
     * Places the player on top of the platform if falling onto it.
     *
     * @param platform The platform collided with
     */
    private void handlePlatformCollision(Platform platform) {
        // Only handle landing on platforms, not hitting them from below or sides
        if (verticalVelocity > 0 && previousY + (getHeight() / 2) <= platform.getY() - (platform.getHeight() / 2)) {
            // Land on platform
            setY(platform.getY() - (platform.getHeight() / 2) - (getHeight() / 2));
            setOnGround(true);
            setVerticalVelocity(0);
        }
    }
    
    /**
     * Handles input from the user to control the player.
     * Processes movement, jumping, and weapon usage.
     *
     * @param input The current input state
     * @param windowWidth The width of the game window
     * @param windowHeight The height of the game window
     */
    public void handleInput(Input input, int windowWidth, int windowHeight) {
        // Only reset horizontal velocity if on ground or ladder (not in mid-air)
        if (onGround || onLadder) {
            horizontalVelocity = 0;
        }
        
        // Handle climbing when on ladder
        if (onLadder && currentLadder != null) {
            // Vertical movement on ladder - fixed speed of 2 pixels per frame
            if (input.isDown(Keys.UP)) {
                setY(getY() - CLIMB_SPEED);
            }
            
            if (input.isDown(Keys.DOWN)) {
                setY(getY() + CLIMB_SPEED);
            }
            
            // Horizontal movement on ladder - standard speed (no reduction)
            if (input.isDown(Keys.LEFT)) {
                if (getX() > getImage().getWidth()/2) {
                    horizontalVelocity = -MOVE_SPEED;
                }
                
                if (facingRight) {
                    facingRight = false;
                    updatePlayerImage();
                }
            }
            
            if (input.isDown(Keys.RIGHT)) {
                if (getX() < windowWidth - getImage().getWidth()/2) {
                    horizontalVelocity = MOVE_SPEED;
                }
                
                if (!facingRight) {
                    facingRight = true;
                    updatePlayerImage();
                }
            }
            
            // No jumping allowed when on ladder
            return;
        }
        
        // Normal movement when not on ladder (includes in-air movement)
        // Handle left movement - allow air control
        if (input.isDown(Keys.LEFT)) {
            if (getX() > getImage().getWidth()/2) { // Boundary check with center-based positioning
                horizontalVelocity = -MOVE_SPEED;
            }
            
            // Update image direction if needed
            if (facingRight) {
                facingRight = false;
                updatePlayerImage();
            }
        }
        
        // Handle right movement - allow air control  
        if (input.isDown(Keys.RIGHT)) {
            if (getX() < windowWidth - getImage().getWidth()/2) { // Boundary check with center-based positioning
                horizontalVelocity = MOVE_SPEED;
            }
            
            // Update image direction if needed
            if (!facingRight) {
                facingRight = true;
                updatePlayerImage();
            }
        }
        
        // Handle jumping - only allow jumping when on the ground
        if (input.wasPressed(Keys.SPACE) && onGround) {
            // Apply initial upward velocity (negative is up)
            verticalVelocity = JUMP_VELOCITY;
            // Player is no longer on the ground
            onGround = false;
        }
    }
    
    /**
     * Updates the player's image based on current state (direction, weapons held).
     */
    private void updatePlayerImage() {
        if (hasHammer) {
            setImage(facingRight ? HAMMER_RIGHT_IMAGE : HAMMER_LEFT_IMAGE);
        } else if (hasBlaster) {
            setImage(facingRight ? BLASTER_RIGHT_IMAGE : BLASTER_LEFT_IMAGE);
        } else {
            setImage(facingRight ? RIGHT_IMAGE : LEFT_IMAGE);
        }
    }
    
    /**
     * Fires a bullet from the player's position.
     * Only works if player has a blaster with bullets remaining.
     *
     * @return A new bullet, or null if no bullets available
     */
    public Bullet fireBullet() {
        if (hasBlaster && bulletCount > 0) {
            // Reduce bullet count
            bulletCount--;
            
            // If out of bullets, revert to normal state
            if (bulletCount <= 0) {
                hasBlaster = false;
                updatePlayerImage();
            }
            
            // Create and return a new bullet
            return new Bullet(getX(), getY(), facingRight);
        }
        
        return null;
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
     *
     * @return true if the player has a hammer, false otherwise
     */
    public boolean hasHammer() {
        return hasHammer;
    }
    
    /**
     * Collects a hammer, granting the player the ability to destroy barrels and defeat Donkey Kong.
     * Updates the player's image to show they are carrying a hammer.
     * Replaces blaster if player had one.
     */
    public void collectHammer() {
        hasHammer = true;
        
        // Replace blaster if we had one
        if (hasBlaster) {
            hasBlaster = false;
            bulletCount = 0;
        }
        
        // Update player image to show hammer
        updatePlayerImage();
    }
    
    /**
     * Checks if the player has a blaster.
     *
     * @return true if the player has a blaster, false otherwise
     */
    public boolean hasBlaster() {
        return hasBlaster;
    }
    
    /**
     * Collects a blaster, granting the player the ability to shoot bullets.
     * Updates the player's image to show they are carrying a blaster.
     * Replaces hammer if player had one.
     *
     * @param initialBullets Number of bullets in the collected blaster
     */
    public void collectBlaster(int initialBullets) {
        hasBlaster = true;
        
        // Replace hammer if we had one
        if (hasHammer) {
            hasHammer = false;
        }
        
        // Add bullets to our count
        bulletCount += initialBullets;
        
        // Update player image to show blaster
        updatePlayerImage();
    }
    
    /**
     * Gets the current bullet count.
     *
     * @return Number of bullets remaining
     */
    public int getBulletCount() {
        return bulletCount;
    }
} 