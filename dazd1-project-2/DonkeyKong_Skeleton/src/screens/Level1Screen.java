package screens;

import bagel.*;
import entities.Barrel;
import entities.Hammer;
import entities.Platform;
import game.GameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Level 1 gameplay screen.
 * Implements level-specific behavior for the first level.
 */
public class Level1Screen extends GameplayScreen {
    // Level 1 Collision Detection Constants
    private static final double BARREL_JUMP_HORIZONTAL_MARGIN = 5.0; // Extra margin for barrel jump detection
    private static final double BARREL_PLAYER_MINIMUM_DISTANCE = 5.0; // Minimum distance to determine player side
    
    // Level 1 specific entities
    private final List<Barrel> barrels = new ArrayList<>();
    private Hammer hammer;
    
    // Barrel jump tracking - track direction of jumps for each barrel
    private final Map<Barrel, Integer> lastJumpDirections = new HashMap<>();
    
    // Track barrel jump statuses
    private static final int JUMP_COOLDOWN = 30; // frames before can jump same barrel again
    private final Map<Barrel, BarrelJumpInfo> barrelJumpStatuses = new HashMap<>();
    
    // Enhanced global jump prevention - more robust system
    private static final int GLOBAL_JUMP_COOLDOWN = 45; // Increased from 15 to 45 frames
    private int globalJumpCooldownTimer = 0; // Global cooldown timer
    private double lastScoredJumpY = Double.MAX_VALUE; // Y position when last jump was scored
    
    // Maximum vertical distance for valid barrel jumps - prevents cross-layer scoring
    private static final double MAX_JUMP_VERTICAL_DISTANCE = 80.0; // Maximum Y-distance between Mario and barrel for valid jump
    
    // Inner class to track jump status for each barrel
    private class BarrelJumpInfo {
        boolean isAboveBarrel = false;     // Player is above the barrel (starting jump)
        boolean hasJumpedOver = false;     // Jump has been recorded
        int cooldownTimer = 0;             // Cooldown before next jump can be scored
        double lastPlayerY = 0;            // Last Y position to track downward movement
        int side = 0;                      // Side player was last on (0=none, -1=left, 1=right)
        boolean jumpInProgress = false;    // Tracks if a jump is currently in progress
    }

    /**
     * Creates a new Level 1 screen.
     *
     * @param gameProps Properties containing game configuration
     * @param messageProps Properties containing game messages
     */
    public Level1Screen(Properties gameProps, Properties messageProps) {
        super(gameProps, messageProps);
        
        // Ensure sub-class fields are initialized before calling init
        init();
        
        if (isDebug()) {
            // Debug: Print all entity positions after initialization
            System.out.println("DEBUG - After initialization:");
            System.out.println("Player: " + getPlayer().getX() + "," + getPlayer().getY());
            System.out.println("DonkeyKong: " + getDonkeyKong().getX() + "," + getDonkeyKong().getY());
            System.out.println("Hammer: " + hammer.getX() + "," + hammer.getY());
            System.out.println("Platforms: " + getPlatforms().size());
            System.out.println("Ladders: " + getLadders().size());
            System.out.println("Barrels: " + barrels.size());
        }
    }

    @Override
    protected String getPlayerPropertyKey() {
        return "mario.level1";
    }

    @Override
    protected String getPlatformsPropertyKey() {
        return "platforms.level1";
    }

    @Override
    protected String getLaddersCountPropertyKey() {
        return "ladder.level1.count";
    }

    @Override
    protected String getLadderPropertyKeyFormat() {
        return "ladder.level1.%d";
    }

    @Override
    protected String getDonkeyKongPropertyKey() {
        return "donkey.level1";
    }

    @Override
    protected void loadLevelEntities() {
        // Initialize barrels using generic method from parent class
        initializeBarrelsForLevel("barrel.level1", barrels);
        
        // Initialize hammer using generic method from parent class
        hammer = initializeHammerForLevel("hammer.level1", 900, 500);
    }
    
    @Override
    protected void correctLevelSpecificPositions() {
        // Position barrels on platforms if they overlap
        for (Barrel barrel : barrels) {
            for (Platform platform : getPlatforms()) {
                if (platform.overlaps(barrel)) {
                    platform.placeEntityOnTop(barrel);
                    barrel.setOnGround(true);
                    break;
                }
            }
        }
        
        // Position hammer on a platform if overlapping
        for (Platform platform : getPlatforms()) {
            if (platform.overlaps(hammer)) {
                platform.placeEntityOnTop(hammer);
                break;
            }
        }
    }
    
    @Override
    protected GameState updateLevelSpecific(Input input) {
        // Update barrels, filter out destroyed ones
        List<Barrel> remainingBarrels = new ArrayList<>();
        for (Barrel barrel : barrels) {
            // Only update and keep barrels that aren't destroyed
            if (!barrel.isDestroyed()) {
                barrel.update();
                remainingBarrels.add(barrel);
            }
        }
        barrels.clear();
        barrels.addAll(remainingBarrels);
        
        // Check for player-hammer collision
        checkHammerCollision();
        
        // Check for barrel jumps
        checkBarrelJumps();
        
        // Check for barrel-platform collisions
        checkBarrelPlatformCollisions();
        
        // Continue gameplay
        return null;
    }
    
    /**
     * Checks for collisions between barrels and platforms.
     * Ensures barrels stay on platforms and don't fall through.
     */
    private void checkBarrelPlatformCollisions() {
        for (Barrel barrel : barrels) {
            if (barrel.isDestroyed()) {
                continue; // Skip destroyed barrels
            }
            
            boolean onPlatform = false;
            
            for (Platform platform : getPlatforms()) {
                if (platform.isCollidingFromTop(barrel)) {
                    platform.placeEntityOnTop(barrel);
                    barrel.setOnGround(true);
                    onPlatform = true;
                    break;
                }
            }
            
            barrel.setOnGround(onPlatform);
        }
    }
    
    /**
     * Checks for collisions between the player and hammer.
     * If the player touches the hammer, they collect it.
     */
    private void checkHammerCollision() {
        if (!hammer.isCollected() && getPlayer().getBoundingBox().intersects(hammer.getBoundingBox())) {
            // Player collected the hammer
            hammer.collect();
            getPlayer().collectHammer();
        }
    }
    
    /**
     * Checks if the player jumps over any barrels.
     * Awards points for successful jumps.
     * Enhanced logic to prevent multiple scores from a single jump, especially near ladders.
     */
    private void checkBarrelJumps() {
        double playerY = getPlayer().getY();
        double playerX = getPlayer().getX();
        
        // Decrement global jump cooldown timer
        if (globalJumpCooldownTimer > 0) {
            globalJumpCooldownTimer--;
        }
        
        for (Barrel barrel : barrels) {
            // Skip destroyed barrels
            if (barrel.isDestroyed()) {
                continue;
            }
            
            double barrelY = barrel.getY();
            double barrelX = barrel.getX();
            double barrelTopY = barrelY - barrel.getHeight()/2;
            
            // Get or create jump info for this barrel
            BarrelJumpInfo jumpInfo = barrelJumpStatuses.computeIfAbsent(barrel, b -> new BarrelJumpInfo());
            
            // Decrement cooldown if active
            if (jumpInfo.cooldownTimer > 0) {
                jumpInfo.cooldownTimer--;
            }
            
            // Calculate horizontal distance from barrel
            boolean isCloseHorizontally = Math.abs(playerX - barrelX) < (getPlayer().getWidth()/2 + barrel.getWidth()/2 + BARREL_JUMP_HORIZONTAL_MARGIN);
            
            // Determine which side of the barrel the player is on
            int currentSide = 0;
            if (Math.abs(playerX - barrelX) > BARREL_PLAYER_MINIMUM_DISTANCE) { // Only if not directly above/below
                currentSide = (playerX > barrelX) ? 1 : -1;
            }
            
            // Player directly above barrel & falling - with vertical distance limit to prevent cross-layer scoring
            boolean playerAboveBarrel = playerY < barrelTopY && getPlayer().getVerticalVelocity() > 0 && 
                Math.abs(playerY - barrelY) <= MAX_JUMP_VERTICAL_DISTANCE;
            
            // Enhanced reset logic - only reset when player is truly on ground and not in a jump
            if (getPlayer().isOnGround() && !getPlayer().isOnLadder() && getPlayer().getVerticalVelocity() <= 0) {
                // Only reset if jump was completed or player has moved significantly away from jump area
                if (jumpInfo.jumpInProgress && 
                    (Math.abs(playerY - lastScoredJumpY) > 20 || !isCloseHorizontally)) {
                    jumpInfo.jumpInProgress = false;
                }
                
                // Record which side of barrel player is on when grounded
                if (!isCloseHorizontally && currentSide != 0) {
                    jumpInfo.side = currentSide;
                }
                
                // Reset jump tracking only if not in middle of a jump
                if (jumpInfo.isAboveBarrel && !jumpInfo.jumpInProgress) {
                    jumpInfo.isAboveBarrel = false;
                    jumpInfo.hasJumpedOver = false;
                }
            }
            
            // Start tracking a potential jump
            if (playerAboveBarrel && isCloseHorizontally && getPlayer().getVerticalVelocity() > 0) {
                jumpInfo.isAboveBarrel = true;
                jumpInfo.lastPlayerY = playerY;
                if (!jumpInfo.jumpInProgress) {
                    jumpInfo.jumpInProgress = true;
                }
            }
            
            // Check if player has moved downward since we started tracking
            boolean movingDownward = playerY > jumpInfo.lastPlayerY;
            jumpInfo.lastPlayerY = playerY;
            
            // Enhanced jump detection with stricter conditions
            if (jumpInfo.isAboveBarrel && movingDownward && !jumpInfo.hasJumpedOver && 
                jumpInfo.cooldownTimer == 0 && globalJumpCooldownTimer == 0 && 
                currentSide != 0 && jumpInfo.side != 0 && 
                currentSide != jumpInfo.side &&
                // Additional safety checks
                Math.abs(playerY - lastScoredJumpY) > 10 && // Ensure different Y position from last score
                getPlayer().getVerticalVelocity() > 0 &&
                // Vertical distance check
                Math.abs(playerY - barrelY) <= MAX_JUMP_VERTICAL_DISTANCE) {
                
                // Award points for jumping over barrel
                getScoreManager().addBarrelJumpScore();
                
                // Mark as jumped and start cooldown for this barrel
                jumpInfo.hasJumpedOver = true;
                jumpInfo.cooldownTimer = JUMP_COOLDOWN;
                
                // Start global cooldown and record position
                globalJumpCooldownTimer = GLOBAL_JUMP_COOLDOWN;
                lastScoredJumpY = playerY;
                
                // Debug output for problematic barrel
                if (isDebug()) {
                    System.out.println("DEBUG - Barrel jump scored at X=" + barrelX + ", Y=" + barrelY + 
                        ", Player Y=" + playerY + ", Side change: " + jumpInfo.side + " -> " + currentSide);
                }
            }
        }
        
        // Clean up destroyed barrels
        barrels.stream()
            .filter(Barrel::isDestroyed)
            .forEach(barrelJumpStatuses::remove);
    }
    
    @Override
    protected GameState checkLevelSpecificCollisions() {
        // Check for player-DK collision
        if (getPlayer().getBoundingBox().intersects(getDonkeyKong().getBoundingBox())) {
            if (getPlayer().hasHammer()) {
                // Win condition: Player touched DK with a hammer
                // Note: Level 1 does not award time bonus - only Level 2 does
                // Base score from Level 1 will carry over to Level 2
                
                // Progress to level 2 instead of showing game over screen
                return GameState.LEVEL2;
            } else {
                // Lose condition: Player touched DK without a hammer
                return GameState.GAME_OVER_LOSE;
            }
        }
        
        // Check for player-barrel collisions
        for (Barrel barrel : barrels) {
            if (getPlayer().getBoundingBox().intersects(barrel.getBoundingBox())) {
                if (getPlayer().hasHammer()) {
                    // Destroy barrel and score points
                    barrel.destroy();
                    getScoreManager().addBarrelDestroyScore();
                } else {
                    // Lose condition: Player touched barrel without a hammer
                    return GameState.GAME_OVER_LOSE;
                }
            }
        }
        
        // No game-ending collisions
        return null;
    }
    
    @Override
    protected void drawLevelSpecific() {
        // Draw barrels
        for (Barrel barrel : barrels) {
            if (!barrel.isDestroyed()) {
                barrel.draw();
            }
        }
        
        // Draw hammer if not collected
        if (!hammer.isCollected()) {
            hammer.draw();
        }
    }
} 