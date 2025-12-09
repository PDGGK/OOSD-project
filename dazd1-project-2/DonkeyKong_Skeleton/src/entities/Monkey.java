package entities;

import bagel.Image;
import bagel.util.Rectangle;
import interfaces.Collidable;
import interfaces.Destroyable;
import interfaces.Movable;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all monkey types.
 * Defines common behaviors like movement, collision detection, and patrol paths.
 */
public abstract class Monkey extends Entity implements Movable, Destroyable {
    // Physics constants
    private static final double GRAVITY = 0.4;
    private static final double TERMINAL_VELOCITY = 5.0;
    private static final double MOVE_SPEED = 0.5;
    private static final int SCORE_VALUE = 100;
    
    // Platform edge detection constant
    private static final double PLATFORM_DETECTION_TOLERANCE = 5.0; // Tolerance for platform edge detection
    
    // Monkey state
    private double verticalVelocity = 0;
    private double horizontalVelocity = 0;
    private boolean onGround = false;
    private boolean facingRight;
    private boolean destroyed = false;
    
    // Patrol path
    private List<Integer> patrolDistances;
    private int currentPathIndex = 0;
    private double distanceTraveled = 0;
    
    /**
     * Creates a new monkey at the specified position.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     * @param initialDirection Initial facing direction (true for right, false for left)
     * @param patrolPath List of patrol distances
     * @param image The monkey image
     */
    public Monkey(double x, double y, boolean initialDirection, List<Integer> patrolPath, Image image) {
        super(x, y, image);
        this.facingRight = initialDirection;
        this.patrolDistances = new ArrayList<>(patrolPath);
        
        // Set initial horizontal velocity based on direction
        this.horizontalVelocity = facingRight ? MOVE_SPEED : -MOVE_SPEED;
    }

    /**
     * Updates the monkey's state.
     * Applies gravity and handles patrol movement.
     */
    @Override
    public void update() {
        if (destroyed) {
            return; // Don't update destroyed monkeys
        }
        
        // Apply movement
        move();
        
        // Update patrol behavior
        patrol();
    }
    
    /**
     * Moves the monkey based on current velocity and state.
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
        
        // Apply horizontal velocity for movement
        setX(getX() + horizontalVelocity);
        
        // Update distance traveled for patrol logic
        distanceTraveled += Math.abs(horizontalVelocity);
    }
    
    /**
     * Sets the monkey's velocity components.
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
     * Gets the horizontal velocity.
     *
     * @return Current horizontal velocity
     */
    @Override
    public double getXVelocity() {
        return horizontalVelocity;
    }
    
    /**
     * Gets the vertical velocity.
     *
     * @return Current vertical velocity
     */
    @Override
    public double getYVelocity() {
        return verticalVelocity;
    }
    
    /**
     * Abstract method to define patrol behavior.
     * Each monkey type may implement its own patrolling strategy.
     */
    protected abstract void patrol();
    
    /**
     * Changes the monkey's direction and updates velocity accordingly.
     */
    protected void changeDirection() {
        facingRight = !facingRight;
        horizontalVelocity = facingRight ? MOVE_SPEED : -MOVE_SPEED;
        updateImage(); // Update image based on new direction
    }
    
    /**
     * Updates the monkey's image based on current direction.
     * Abstract method to be implemented by subclasses.
     */
    protected abstract void updateImage();
    
    /**
     * Handles collision with another entity.
     *
     * @param other The entity this monkey has collided with
     */
    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Platform) {
            // Handle platform collision
            handlePlatformCollision((Platform) other);
        }
    }
    
    /**
     * Handles collision with a platform.
     *
     * @param platform The platform collided with
     */
    private void handlePlatformCollision(Platform platform) {
        // Land on platform from above
        if (verticalVelocity > 0) {
            platform.placeEntityOnTop(this);
            setOnGround(true);
        }
    }
    
    /**
     * Checks if this monkey is at the edge of a platform.
     * Used to prevent falling off platforms.
     *
     * @param platforms List of platforms to check
     * @return true if monkey is at a platform edge
     */
    private boolean isAtPlatformEdge(List<Platform> platforms) {
        // Calculate position after potential move
        double futureX = getX() + horizontalVelocity;
        double monkeyWidth = getWidth();
        double monkeyHalfWidth = monkeyWidth / 2;
        
        // Check if the monkey would still be on any platform
        for (Platform platform : platforms) {
            double platformLeft = platform.getX() - platform.getWidth() / 2;
            double platformRight = platform.getX() + platform.getWidth() / 2;
            double platformTop = platform.getY() - platform.getHeight() / 2;
            
            // If standing on this platform
            if (Math.abs(getY() + getHeight()/2 - platformTop) < PLATFORM_DETECTION_TOLERANCE) {
                // Check if future position would be off the platform
                if ((facingRight && (futureX + monkeyHalfWidth > platformRight)) || 
                    (!facingRight && (futureX - monkeyHalfWidth < platformLeft))) {
                    return true; // At edge
                }
            }
        }
        return false;
    }
    
    /**
     * Sets whether the monkey is on the ground.
     *
     * @param onGround true if on a platform, false otherwise
     */
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    /**
     * Checks if the monkey is on the ground.
     *
     * @return true if on the ground, false otherwise
     */
    public boolean isOnGround() {
        return onGround;
    }
    
    /**
     * Marks this monkey as destroyed.
     * Implements the Destroyable interface.
     */
    @Override
    public void destroy() {
        destroyed = true;
    }
    
    /**
     * Checks if this monkey has been destroyed.
     * Implements the Destroyable interface.
     *
     * @return true if destroyed, false otherwise
     */
    @Override
    public boolean isDestroyed() {
        return destroyed;
    }
    
    /**
     * Gets the score value for destroying this monkey.
     * Implements the Destroyable interface.
     *
     * @return The score value (100 points)
     */
    @Override
    public int getScoreValue() {
        return SCORE_VALUE;
    }
    
    /**
     * Checks if the monkey is facing right.
     *
     * @return true if facing right, false if facing left
     */
    public boolean isFacingRight() {
        return facingRight;
    }
    
    /**
     * Gets the patrol distances list.
     *
     * @return List of patrol distances
     */
    public List<Integer> getPatrolDistances() {
        return new ArrayList<>(patrolDistances);
    }
    
    /**
     * Gets the current path index.
     *
     * @return Current path index
     */
    public int getCurrentPathIndex() {
        return currentPathIndex;
    }
    
    /**
     * Sets the current path index.
     *
     * @param currentPathIndex The new path index
     */
    public void setCurrentPathIndex(int currentPathIndex) {
        this.currentPathIndex = currentPathIndex;
    }
    
    /**
     * Gets the distance traveled.
     *
     * @return Distance traveled
     */
    public double getDistanceTraveled() {
        return distanceTraveled;
    }
    
    /**
     * Sets the distance traveled.
     *
     * @param distanceTraveled The new distance traveled
     */
    public void setDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }
    
    /**
     * Checks platform edges for this monkey.
     * Public method for external classes to call.
     *
     * @param platforms List of platforms to check
     */
    public void checkPlatformEdges(List<Platform> platforms) {
        if (isAtPlatformEdge(platforms)) {
            changeDirection();
            distanceTraveled = 0;
            
            // Move to next patrol distance
            currentPathIndex = (currentPathIndex + 1) % patrolDistances.size();
        }
    }
} 