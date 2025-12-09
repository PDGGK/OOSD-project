package entities;

import bagel.Image;
import java.util.List;

/**
 * Represents an intelligent monkey that patrols platforms and shoots bananas.
 * Intelligent monkeys shoot bananas every 5 seconds.
 */
public class IntelligentMonkey extends Monkey {
    private static final Image MONKEY_RIGHT_IMAGE = new Image("res/intelli_monkey_right.png");
    private static final Image MONKEY_LEFT_IMAGE = new Image("res/intelli_monkey_left.png");
    
    // Shooting behavior
    private static final long BANANA_INTERVAL = 5000; // 5 seconds in milliseconds
    private long lastBananaTime = 0;
    
    /**
     * Creates a new intelligent monkey at the specified position.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     * @param initialDirection Initial facing direction ("right" or "left")
     * @param patrolPath List of patrol distances
     */
    public IntelligentMonkey(double x, double y, String initialDirection, List<Integer> patrolPath) {
        super(x, y, "right".equalsIgnoreCase(initialDirection), 
              patrolPath, 
              "right".equalsIgnoreCase(initialDirection) ? MONKEY_RIGHT_IMAGE : MONKEY_LEFT_IMAGE);
        
        // Initialize timer for banana shooting
        lastBananaTime = System.currentTimeMillis();
    }
    
    /**
     * Updates the monkey's state and shooting behavior.
     */
    @Override
    public void update() {
        super.update();
        
        // Don't update further if destroyed
        if (isDestroyed()) {
            return;
        }
    }
    
    /**
     * Updates the patrol behavior for this monkey.
     * Follows the patrol path specified during creation.
     */
    @Override
    protected void patrol() {
        List<Integer> patrolDistances = getPatrolDistances();
        if (patrolDistances.isEmpty() || getCurrentPathIndex() >= patrolDistances.size()) {
            return; // No path to follow or reached end of path
        }
        
        int currentPatrolDistance = patrolDistances.get(getCurrentPathIndex());
        
        // Check if reached current patrol distance
        if (getDistanceTraveled() >= currentPatrolDistance) {
            // Reset distance and change direction
            setDistanceTraveled(0);
            changeDirection();
            
            // Move to next patrol distance, loop back if at end
            setCurrentPathIndex((getCurrentPathIndex() + 1) % patrolDistances.size());
        }
    }
    
    /**
     * Updates the monkey's image based on direction.
     */
    @Override
    protected void updateImage() {
        setImage(isFacingRight() ? MONKEY_RIGHT_IMAGE : MONKEY_LEFT_IMAGE);
    }
    
    /**
     * Checks if this monkey can throw a banana based on time interval.
     *
     * @return true if enough time has passed since the last throw
     */
    public boolean canThrowBanana() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBananaTime >= BANANA_INTERVAL) {
            lastBananaTime = currentTime;
            return true;
        }
        return false;
    }
    
    /**
     * Creates a banana projectile to throw in the monkey's facing direction.
     *
     * @return A new banana projectile
     */
    public Banana throwBanana() {
        if (canThrowBanana() && !isDestroyed()) {
            return new Banana(getX(), getY(), isFacingRight());
        }
        return null;
    }
} 