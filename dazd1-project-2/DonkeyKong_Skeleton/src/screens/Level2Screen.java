package screens;

import bagel.*;
import entities.Barrel;
import entities.Banana;
import entities.Blaster;
import entities.Bullet;
import entities.Hammer;
import entities.IntelligentMonkey;
import entities.Monkey;
import entities.NormalMonkey;
import entities.Platform;
import game.GameState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Level 2 gameplay screen.
 * Implements level-specific behavior for the second level.
 * Includes monkeys and blaster mechanics.
 */
public class Level2Screen extends GameplayScreen {
    // Level 2 Barrel Jump Constants (same as Level 1)
    private static final double BARREL_JUMP_HORIZONTAL_MARGIN = 5.0; // Extra margin for barrel jump detection
    private static final double BARREL_PLAYER_MINIMUM_DISTANCE = 5.0; // Minimum distance to determine player side
    
    // Track barrel jump statuses
    private static final int JUMP_COOLDOWN = 30; // frames before can jump same barrel again
    
    // Enhanced global jump prevention - more robust system
    private static final int GLOBAL_JUMP_COOLDOWN = 45; // Increased from 15 to 45 frames
    private int globalJumpCooldownTimer = 0; // Global cooldown timer
    private double lastScoredJumpY = Double.MAX_VALUE; // Y position when last jump was scored
    
    // Maximum vertical distance for valid barrel jumps - prevents cross-layer scoring
    private static final double MAX_JUMP_VERTICAL_DISTANCE = 80.0; // Maximum Y-distance between Mario and barrel for valid jump
    
    // Level 2 specific entities
    private final List<Barrel> barrels = new ArrayList<>();
    private Hammer hammer;
    
    // Barrel jump tracking for Level 2
    private final Map<Barrel, BarrelJumpInfo> barrelJumpStatuses = new HashMap<>();
    
    // Inner class to track jump status for each barrel (same as Level 1)
    private class BarrelJumpInfo {
        boolean isAboveBarrel = false;     // Player is above the barrel (starting jump)
        boolean hasJumpedOver = false;     // Jump has been recorded
        int cooldownTimer = 0;             // Cooldown before next jump can be scored
        double lastPlayerY = 0;            // Last Y position to track downward movement
        int side = 0;                      // Side player was last on (0=none, -1=left, 1=right)
        boolean jumpInProgress = false;    // Tracks if a jump is currently in progress
    }
    
    // Monkey entities for Level 2
    private final List<NormalMonkey> normalMonkeys = new ArrayList<>();
    private final List<IntelligentMonkey> intelligentMonkeys = new ArrayList<>();
    
    // Projectile entities
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Banana> bananas = new ArrayList<>();
    
    // Weapon entities
    private final List<Blaster> blasters = new ArrayList<>();
    
    // UI elements
    private final Font healthFont;
    private final int healthX;
    private final int healthY;
    private final Font bulletFont;
    private final int bulletX;
    private final int bulletY;
    
    /**
     * Creates a new Level 2 screen.
     *
     * @param gameProps Properties containing game configuration
     * @param messageProps Properties containing game messages
     */
    public Level2Screen(Properties gameProps, Properties messageProps) {
        super(gameProps, messageProps);
        
        // Initialize health display
        healthFont = createFont("gamePlay.score.fontSize");
        String healthCoords = gameProps.getProperty("gamePlay.donkeyhealth.coords");
        String[] healthCoordArray = healthCoords.split(",");
        healthX = Integer.parseInt(healthCoordArray[0]);
        healthY = Integer.parseInt(healthCoordArray[1]);
        
        // Initialize bullet count display (positioned below DK health)
        bulletFont = healthFont;
        bulletX = healthX;
        bulletY = healthY + 30; // 30 pixels below DK health display
        
        // Initialize level-specific components
        init();
        
        if (isDebug()) {
            // Debug: Print all entity positions after initialization
            System.out.println("DEBUG - After Level 2 initialization:");
            System.out.println("Player: " + getPlayer().getX() + "," + getPlayer().getY());
            System.out.println("DonkeyKong: " + getDonkeyKong().getX() + "," + getDonkeyKong().getY());
            System.out.println("DonkeyKong Health: " + getDonkeyKong().getHealth());
            System.out.println("Health Display: " + healthX + "," + healthY);
            System.out.println("Bullet Display: " + bulletX + "," + bulletY);
            System.out.println("Platforms: " + getPlatforms().size());
            System.out.println("Ladders: " + getLadders().size());
            System.out.println("Normal Monkeys: " + normalMonkeys.size());
            System.out.println("Intelligent Monkeys: " + intelligentMonkeys.size());
            System.out.println("Blasters: " + blasters.size());
        }
    }

    @Override
    protected String getPlayerPropertyKey() {
        return "mario.level2";
    }

    @Override
    protected String getPlatformsPropertyKey() {
        return "platforms.level2";
    }

    @Override
    protected String getLaddersCountPropertyKey() {
        return "ladder.level2.count";
    }

    @Override
    protected String getLadderPropertyKeyFormat() {
        return "ladder.level2.%d";
    }

    @Override
    protected String getDonkeyKongPropertyKey() {
        return "donkey.level2";
    }

    @Override
    protected void loadLevelEntities() {
        // Initialize barrels using generic method from parent class
        initializeBarrelsForLevel("barrel.level2", barrels);
        
        // Initialize hammer using generic method from parent class
        hammer = initializeHammerForLevel("hammer.level2", 900, 500);
        
        // Initialize level 2 specific entities
        initializeNormalMonkeys();
        initializeIntelligentMonkeys();
        initializeBlasters();
    }
    
    /**
     * Generic method to initialize monkeys for Level 2.
     * Reduces code duplication between normal and intelligent monkey initialization.
     *
     * @param typePrefix The monkey type prefix (e.g., "normalMonkey", "intelligentMonkey")
     * @param monkeyList The list to add monkeys to
     * @param isIntelligent Whether to create intelligent monkeys (true) or normal monkeys (false)
     */
    @SuppressWarnings("unchecked")
    private <T extends Monkey> void initializeMonkeysForLevel2(String typePrefix, List<T> monkeyList, boolean isIntelligent) {
        int monkeyCount = Integer.parseInt(getGameProps().getProperty(typePrefix + ".level2.count", "0"));
        
        if (isDebug()) {
            System.out.println("DEBUG - Level 2 " + typePrefix + " count: " + monkeyCount);
        }
        
        for (int i = 1; i <= monkeyCount; i++) {
            String monkeyConfig = getGameProps().getProperty(typePrefix + ".level2." + i);
            
            if (monkeyConfig != null && !monkeyConfig.isEmpty()) {
                String[] parts = monkeyConfig.split(";");
                if (parts.length >= 3) {
                    try {
                        // Parse coordinates
                        String[] coords = parts[0].split(",");
                        double x = Double.parseDouble(coords[0]);
                        double y = Double.parseDouble(coords[1]);
                        
                        // Parse direction
                        String direction = parts[1];
                        
                        // Parse route distances
                        String[] distanceStrings = parts[2].split(",");
                        List<Integer> patrolDistances = Arrays.stream(distanceStrings)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                        
                        // Create the appropriate monkey type
                        T monkey;
                        if (isIntelligent) {
                            monkey = (T) new IntelligentMonkey(x, y, direction, patrolDistances);
                        } else {
                            monkey = (T) new NormalMonkey(x, y, direction, patrolDistances);
                        }
                        
                        monkeyList.add(monkey);
                        
                        if (isDebug()) {
                            System.out.println("DEBUG - Added Level 2 " + typePrefix + " " + i + 
                                ": " + x + "," + y + ", direction: " + direction + 
                                ", route: " + patrolDistances);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing " + typePrefix + " coordinates: " + monkeyConfig);
                    }
                }
            }
        }
    }

    /**
     * Initializes normal monkeys from the properties file.
     */
    private void initializeNormalMonkeys() {
        initializeMonkeysForLevel2("normalMonkey", normalMonkeys, false);
    }
    
    /**
     * Initializes intelligent monkeys from the properties file.
     */
    private void initializeIntelligentMonkeys() {
        initializeMonkeysForLevel2("intelligentMonkey", intelligentMonkeys, true);
    }
    
    /**
     * Initializes blasters from the properties file.
     */
    private void initializeBlasters() {
        int blasterCount = Integer.parseInt(getGameProps().getProperty("blaster.level2.count", "0"));
        
        if (isDebug()) {
            System.out.println("DEBUG - Level 2 Blaster count: " + blasterCount);
        }
        
        for (int i = 1; i <= blasterCount; i++) {
            String blasterPos = getGameProps().getProperty("blaster.level2." + i);
            
            if (blasterPos != null && !blasterPos.isEmpty()) {
                String[] coords = blasterPos.split(",");
                if (coords.length == 2) {
                    try {
                        double x = Double.parseDouble(coords[0]);
                        double y = Double.parseDouble(coords[1]);
                        blasters.add(new Blaster(x, y));
                        
                        if (isDebug()) {
                            System.out.println("DEBUG - Added Level 2 blaster " + i + ": " + x + "," + y);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing blaster coordinates: " + blasterPos);
                    }
                }
            }
        }
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
        if (hammer != null) {
            for (Platform platform : getPlatforms()) {
                if (platform.overlaps(hammer)) {
                    platform.placeEntityOnTop(hammer);
                    break;
                }
            }
        }
        
        // Position normal monkeys on platforms if they overlap
        for (NormalMonkey monkey : normalMonkeys) {
            for (Platform platform : getPlatforms()) {
                if (platform.overlaps(monkey)) {
                    platform.placeEntityOnTop(monkey);
                    monkey.setOnGround(true);
                    break;
                }
            }
        }
        
        // Position intelligent monkeys on platforms if they overlap
        for (IntelligentMonkey monkey : intelligentMonkeys) {
            for (Platform platform : getPlatforms()) {
                if (platform.overlaps(monkey)) {
                    platform.placeEntityOnTop(monkey);
                    monkey.setOnGround(true);
                    break;
                }
            }
        }
        
        // Position blasters on platforms if they overlap
        for (Blaster blaster : blasters) {
            for (Platform platform : getPlatforms()) {
                if (platform.overlaps(blaster)) {
                    platform.placeEntityOnTop(blaster);
                    break;
                }
            }
        }
    }
    
    @Override
    protected GameState updateLevelSpecific(Input input) {
        // Update barrels
        for (Barrel barrel : barrels) {
            if (!barrel.isDestroyed()) {
                barrel.update();
            }
        }
        
        // Update normal monkeys
        for (NormalMonkey monkey : normalMonkeys) {
            if (!monkey.isDestroyed()) {
                // Check platform edges before updating
                monkey.checkPlatformEdges(getPlatforms());
                monkey.update();
            }
        }
        
        // Update intelligent monkeys and handle banana throwing
        updateIntelligentMonkeys();
        
        // Update blasters
        for (Blaster blaster : blasters) {
            blaster.update();
        }
        
        // Check for player-hammer collision
        if (hammer != null && !hammer.isCollected() && 
            getPlayer().getBoundingBox().intersects(hammer.getBoundingBox())) {
            hammer.collect();
            getPlayer().collectHammer();
        }
        
        // Check for player-blaster collision
        checkBlasterCollisions();
        
        // Check for barrel jumps (add 30 points for jumping over barrels)
        checkBarrelJumps();
        
        // Handle shooting
        handleShooting(input);
        
        // Update bullets
        updateBullets();
        
        // Update bananas
        updateBananas();
        
        // Check for barrel-platform collisions
        for (Barrel barrel : barrels) {
            if (barrel.isDestroyed()) {
                continue;
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
        
        // Check for monkey-platform collisions
        updateMonkeyPlatformCollisions();
        
        return null;
    }
    
    /**
     * Updates intelligent monkeys and handles banana throwing.
     */
    private void updateIntelligentMonkeys() {
        for (IntelligentMonkey monkey : intelligentMonkeys) {
            if (!monkey.isDestroyed()) {
                // Check platform edges before updating
                monkey.checkPlatformEdges(getPlatforms());
                monkey.update();
                
                // Check if this monkey should throw a banana
                Banana banana = monkey.throwBanana();
                if (banana != null) {
                    bananas.add(banana);
                    
                    if (isDebug()) {
                        System.out.println("DEBUG - Monkey threw banana at: " + banana.getX() + "," + banana.getY());
                    }
                }
            }
        }
    }
    
    /**
     * Updates bananas and removes inactive ones.
     */
    private void updateBananas() {
        // Update and filter bananas
        List<Banana> activeBananas = new ArrayList<>();
        for (Banana banana : bananas) {
            if (banana.isActive()) {
                banana.update();
                checkBananaCollisions(banana);
                
                // Only keep if still active after collision check
                if (banana.isActive()) {
                    activeBananas.add(banana);
                }
            }
        }
        bananas.clear();
        bananas.addAll(activeBananas);
    }
    
    /**
     * Checks collisions for a specific banana.
     *
     * @param banana The banana to check collisions for
     */
    private void checkBananaCollisions(Banana banana) {
        // Check for banana hitting screen edges
        if (banana.getX() < 0 || banana.getX() > getWindowWidth()) {
            banana.deactivate();
            return;
        }
        
        // Check for banana-platform collisions
        for (Platform platform : getPlatforms()) {
            if (banana.getBoundingBox().intersects(platform.getBoundingBox())) {
                banana.deactivate();
                return;
            }
        }
        
        // Note: Player-banana collision is handled in checkLevelSpecificCollisions()
        // to ensure proper game state management
    }
    
    /**
     * Checks for collisions between monkeys and platforms.
     * Ensures monkeys stay on platforms and don't fall through.
     */
    private void updateMonkeyPlatformCollisions() {
        // Check normal monkey-platform collisions
        for (NormalMonkey monkey : normalMonkeys) {
            if (monkey.isDestroyed()) {
                continue;
            }
            
            boolean onPlatform = false;
            for (Platform platform : getPlatforms()) {
                if (platform.isCollidingFromTop(monkey)) {
                    platform.placeEntityOnTop(monkey);
                    monkey.setOnGround(true);
                    onPlatform = true;
                    break;
                }
            }
            monkey.setOnGround(onPlatform);
        }
        
        // Check intelligent monkey-platform collisions
        for (IntelligentMonkey monkey : intelligentMonkeys) {
            if (monkey.isDestroyed()) {
                continue;
            }
            
            boolean onPlatform = false;
            for (Platform platform : getPlatforms()) {
                if (platform.isCollidingFromTop(monkey)) {
                    platform.placeEntityOnTop(monkey);
                    monkey.setOnGround(true);
                    onPlatform = true;
                    break;
                }
            }
            monkey.setOnGround(onPlatform);
        }
    }
    
    /**
     * Checks for collisions between blasters and the player.
     */
    private void checkBlasterCollisions() {
        for (int i = blasters.size() - 1; i >= 0; i--) {
            Blaster blaster = blasters.get(i);
            
            if (!blaster.isCollected() && getPlayer().getBoundingBox().intersects(blaster.getBoundingBox())) {
                // Collect the blaster
                blaster.collect();
                getPlayer().collectBlaster(blaster.getInitialBulletCount());
            }
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
                getPlayer().getVerticalVelocity() > 0 && // Ensure player is still falling
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
                    System.out.println("DEBUG - Level 2 Barrel jump scored at X=" + barrelX + ", Y=" + barrelY + 
                        ", Player Y=" + playerY + ", Side change: " + jumpInfo.side + " -> " + currentSide);
                }
            }
        }
        
        // Clean up destroyed barrels
        barrels.stream()
            .filter(Barrel::isDestroyed)
            .forEach(barrelJumpStatuses::remove);
    }
    
    /**
     * Handles shooting input and creates bullets.
     */
    private void handleShooting(Input input) {
        // Process shooting
        if (input.wasPressed(Keys.S) && getPlayer().hasBlaster()) {
            Bullet bullet = getPlayer().fireBullet();
            if (bullet != null) {
                bullets.add(bullet);
            }
        }
    }
    
    /**
     * Updates bullets and removes inactive ones.
     */
    private void updateBullets() {
        // Update and filter bullets
        List<Bullet> activeBullets = new ArrayList<>();
        for (Bullet bullet : bullets) {
            if (bullet.isActive()) {
                bullet.update();
                checkBulletCollisions(bullet);
                
                // Only keep if still active after collision check
                if (bullet.isActive()) {
                    activeBullets.add(bullet);
                }
            }
        }
        bullets.clear();
        bullets.addAll(activeBullets);
    }
    
    /**
     * Checks collisions for a specific bullet.
     *
     * @param bullet The bullet to check collisions for
     */
    private void checkBulletCollisions(Bullet bullet) {
        // Check for bullet hitting screen edges
        if (bullet.getX() < 0 || bullet.getX() > getWindowWidth()) {
            bullet.deactivate();
            return;
        }
        
        // Check for bullet-platform collisions
        for (Platform platform : getPlatforms()) {
            if (bullet.getBoundingBox().intersects(platform.getBoundingBox())) {
                bullet.deactivate();
                return;
            }
        }
        
        // Check for bullet-normal monkey collisions
        for (NormalMonkey monkey : normalMonkeys) {
            if (!monkey.isDestroyed() && bullet.getBoundingBox().intersects(monkey.getBoundingBox())) {
                monkey.destroy();
                bullet.deactivate();
                getScoreManager().addScore(monkey.getScoreValue());
                return;
            }
        }
        
        // Check for bullet-intelligent monkey collisions
        for (IntelligentMonkey monkey : intelligentMonkeys) {
            if (!monkey.isDestroyed() && bullet.getBoundingBox().intersects(monkey.getBoundingBox())) {
                monkey.destroy();
                bullet.deactivate();
                getScoreManager().addScore(monkey.getScoreValue());
                return;
            }
        }
        
        // Check for bullet-DK collisions
        if (bullet.getBoundingBox().intersects(getDonkeyKong().getBoundingBox())) {
            // Deal damage to Donkey Kong
            getDonkeyKong().takeDamage(bullet.getDamage());
            bullet.deactivate();
            return;
        }
    }
    
    @Override
    protected GameState checkLevelSpecificCollisions() {
        // Check for player-DK collision
        if (getPlayer().getBoundingBox().intersects(getDonkeyKong().getBoundingBox())) {
            if (getPlayer().hasHammer()) {
                // Win condition: Player touched DK with a hammer
                // Defeat DK instantly
                getDonkeyKong().defeat();
                
                // Add time bonus before returning game over state
                int remainingSeconds = getRemainingTime();
                getScoreManager().addTimeBonus(remainingSeconds);
                return GameState.GAME_OVER_WIN;
            } else {
                // Lose condition: Player touched DK without a hammer
                return GameState.GAME_OVER_LOSE;
            }
        }
        
        // Check if DK is dead (health reduced to 0)
        if (getDonkeyKong().isDead()) {
            // Win condition: DK's health reached 0
            // Make sure time bonus is calculated only once
            if (!getScoreManager().hasTimeBonus()) {
                int remainingSeconds = getRemainingTime();
                getScoreManager().addTimeBonus(remainingSeconds);
            }
            return GameState.GAME_OVER_WIN;
        }
        
        // Check for player-barrel collisions
        for (Barrel barrel : barrels) {
            if (!barrel.isDestroyed() && getPlayer().getBoundingBox().intersects(barrel.getBoundingBox())) {
                if (getPlayer().hasHammer()) {
                    // Destroy barrel and score points
                    barrel.destroy();
                    getScoreManager().addScore(barrel.getScoreValue()); // 100 points for destroying a barrel
                } else {
                    // Lose condition: Player touched barrel without a hammer
                    return GameState.GAME_OVER_LOSE;
                }
            }
        }
        
        // Check for player-normal monkey collisions
        for (NormalMonkey monkey : normalMonkeys) {
            if (!monkey.isDestroyed() && getPlayer().getBoundingBox().intersects(monkey.getBoundingBox())) {
                if (getPlayer().hasHammer()) {
                    // Destroy monkey and score points
                    monkey.destroy();
                    getScoreManager().addScore(monkey.getScoreValue()); // 100 points for destroying a monkey
                } else {
                    // Lose condition: Player touched monkey without a hammer
                    return GameState.GAME_OVER_LOSE;
                }
            }
        }
        
        // Check for player-intelligent monkey collisions
        for (IntelligentMonkey monkey : intelligentMonkeys) {
            if (!monkey.isDestroyed() && getPlayer().getBoundingBox().intersects(monkey.getBoundingBox())) {
                if (getPlayer().hasHammer()) {
                    // Destroy monkey and score points
                    monkey.destroy();
                    getScoreManager().addScore(monkey.getScoreValue()); // 100 points for destroying a monkey
                } else {
                    // Lose condition: Player touched monkey without a hammer
                    return GameState.GAME_OVER_LOSE;
                }
            }
        }
        
        // Check for player-banana collisions (bananas always damage Mario)
        for (Banana banana : bananas) {
            if (banana.isActive() && getPlayer().getBoundingBox().intersects(banana.getBoundingBox())) {
                // Lose condition: Player touched banana (even with hammer)
                banana.deactivate(); // Deactivate the banana that caused the collision
                return GameState.GAME_OVER_LOSE;
            }
        }
        
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
        if (hammer != null && !hammer.isCollected()) {
            hammer.draw();
        }
        
        // Draw blasters if not collected
        for (Blaster blaster : blasters) {
            blaster.draw();
        }
        
        // Draw normal monkeys
        for (NormalMonkey monkey : normalMonkeys) {
            if (!monkey.isDestroyed()) {
                monkey.draw();
            }
        }
        
        // Draw intelligent monkeys
        for (IntelligentMonkey monkey : intelligentMonkeys) {
            if (!monkey.isDestroyed()) {
                monkey.draw();
            }
        }
        
        // Draw bullets
        for (Bullet bullet : bullets) {
            bullet.draw();
        }
        
        // Draw bananas
        for (Banana banana : bananas) {
            banana.draw();
        }
        
        // Draw DK health
        String healthText = "Donkey Health " + getDonkeyKong().getHealth();
        healthFont.drawString(healthText, healthX, healthY);
        
        // Draw bullet count
        String bulletText = "Bullet " + getPlayer().getBulletCount();
        bulletFont.drawString(bulletText, bulletX, bulletY);
    }
} 