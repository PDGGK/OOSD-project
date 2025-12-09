package entities;

import bagel.Image;
import java.util.List;

/**
 * Represents a normal monkey that patrols platforms.
 * Normal monkeys move horizontally and don't attack.
 */
public class NormalMonkey extends Monkey {
    private static final Image MONKEY_RIGHT_IMAGE = new Image("res/normal_monkey_right.png");
    private static final Image MONKEY_LEFT_IMAGE = new Image("res/normal_monkey_left.png");
    
    /**
     * Creates a new normal monkey at the specified position.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     * @param initialDirection Initial facing direction ("right" or "left")
     * @param patrolPath List of patrol distances
     */
    public NormalMonkey(double x, double y, String initialDirection, List<Integer> patrolPath) {
        super(x, y, "right".equalsIgnoreCase(initialDirection), 
              patrolPath, 
              "right".equalsIgnoreCase(initialDirection) ? MONKEY_RIGHT_IMAGE : MONKEY_LEFT_IMAGE);
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
} 