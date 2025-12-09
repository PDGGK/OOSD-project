package interfaces;

/**
 * Interface for weapons that can be collected and used by the player.
 * Defines standard weapon behaviors.
 */
public interface Weapon {
    /**
     * Checks if the weapon has been collected.
     *
     * @return true if collected, false otherwise
     */
    boolean isCollected();
    
    /**
     * Marks the weapon as collected.
     */
    void collect();
    
    /**
     * Uses the weapon's ability.
     */
    void use();
    
    /**
     * Checks if the weapon is currently active.
     *
     * @return true if the weapon is active, false otherwise
     */
    boolean isActive();
} 