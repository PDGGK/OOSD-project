package screens;

import bagel.*;
import entities.Player;
import entities.Platform;
import entities.Ladder;
import entities.DonkeyKong;
import entities.Barrel;
import entities.Hammer;
import game.GameState;
import game.ScoreManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * The main gameplay screen where the game action happens.
 * Manages all game entities and their interactions.
 */
public class GameplayScreen extends Screen {
    private final Player player;
    private final int windowWidth;
    private final int windowHeight;
    
    private final Font scoreFont;
    private final int scoreX;
    private final int scoreY;
    private final int timeY;
    
    private int currentFrame = 0;
    private final int maxFrames;
    
    // Game elements
    private final List<Platform> platforms = new ArrayList<>();
    private final List<Ladder> ladders = new ArrayList<>();
    private final List<Barrel> barrels = new ArrayList<>();
    private DonkeyKong donkeyKong;
    private Hammer hammer;
    
    // Scoring system
    private final ScoreManager scoreManager;
    
    // Barrel jump tracking - track direction of jumps for each barrel
    private final Map<Barrel, Integer> lastJumpDirections = new HashMap<>();
    
    // Track barrel jump statuses
    private static final int JUMP_COOLDOWN = 30; // frames before can jump same barrel again
    private final Map<Barrel, BarrelJumpInfo> barrelJumpStatuses = new HashMap<>();
    
    // Inner class to track jump status for each barrel
    private class BarrelJumpInfo {
        boolean isAboveBarrel = false;     // Player is above the barrel (starting jump)
        boolean hasJumpedOver = false;     // Jump has been recorded
        int cooldownTimer = 0;             // Cooldown before next jump can be scored
        double lastPlayerY = 0;            // Last Y position to track downward movement
        int side = 0;                      // Side player was last on (0=none, -1=left, 1=right)
    }
    
    /**
     * Creates a new gameplay screen.
     *
     * @param gameProps Properties containing game configuration
     * @param messageProps Properties containing game messages
     */
    public GameplayScreen(Properties gameProps, Properties messageProps) {
        super(gameProps, messageProps);
        
        // Initialize window dimensions
        this.windowWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        this.windowHeight = Integer.parseInt(gameProps.getProperty("window.height"));
        
        // Initialize player
        String playerPosStr = gameProps.getProperty("mario.level1");
        String[] playerPos = playerPosStr.split(",");
        double playerStartX = Double.parseDouble(playerPos[0]);
        double playerStartY = Double.parseDouble(playerPos[1]);
        player = new Player(playerStartX, playerStartY);
        System.out.println("DEBUG - Mario position: " + playerStartX + "," + playerStartY);
        
        // Initialize score display
        scoreFont = loadFont("gamePlay.score.fontSize");
        scoreX = Integer.parseInt(gameProps.getProperty("gamePlay.score.x"));
        scoreY = Integer.parseInt(gameProps.getProperty("gamePlay.score.y"));
        timeY = scoreY + 30; // 30 pixels below score
        
        // Initialize game timer
        maxFrames = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames"));
        
        // Initialize score manager
        scoreManager = new ScoreManager();
        
        // Initialize platforms
        initializePlatforms(gameProps);
        
        // Initialize ladders
        initializeLadders(gameProps);
        
        // Initialize Donkey Kong
        initializeDonkeyKong(gameProps);
        
        // Initialize barrels
        initializeBarrels(gameProps);
        
        // Initialize hammer
        initializeHammer(gameProps);
        
        // Initial position correction for all entities
        correctInitialPositions();
        
        // Debug: Print all entity positions after initialization
        System.out.println("DEBUG - After initialization:");
        System.out.println("Player: " + player.getX() + "," + player.getY());
        System.out.println("DonkeyKong: " + donkeyKong.getX() + "," + donkeyKong.getY());
        System.out.println("Hammer: " + hammer.getX() + "," + hammer.getY());
        System.out.println("Platforms: " + platforms.size());
        System.out.println("Ladders: " + ladders.size());
        System.out.println("Barrels: " + barrels.size());
    }
    
    /**
     * Initializes platforms from the properties file.
     * Platform data is in the format "x1,y1;x2,y2;x3,y3;..."
     *
     * @param props Properties containing platform configuration
     */
    private void initializePlatforms(Properties props) {
        String platformsStr = props.getProperty("platforms.level1");
        System.out.println("DEBUG - Platform string: " + platformsStr);
        
        if (platformsStr != null && !platformsStr.isEmpty()) {
            // Split the string by semicolons to get each platform
            String[] platformArray = platformsStr.split(";");
            
            for (String platform : platformArray) {
                // Split the platform by comma to get x and y coordinates
                String[] coords = platform.split(",");
                if (coords.length == 2) {
                    try {
                        double x = Double.parseDouble(coords[0]);
                        double y = Double.parseDouble(coords[1]);
                        platforms.add(new Platform(x, y));
                        System.out.println("DEBUG - Added platform: " + x + "," + y);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing platform coordinates: " + platform);
                    }
                }
            }
        }
    }
    
    /**
     * Initializes ladders from the properties file.
     * Ladder format uses "ladder.level1.count" for count and "ladder.level1.1=x,y" etc. for positions
     *
     * @param props Properties containing ladder configuration
     */
    private void initializeLadders(Properties props) {
        int ladderCount = Integer.parseInt(props.getProperty("ladder.level1.count", "0"));
        System.out.println("DEBUG - Ladder count: " + ladderCount);
        
        for (int i = 1; i <= ladderCount; i++) {
            String ladderPos = props.getProperty("ladder.level1." + i);
            System.out.println("DEBUG - Ladder " + i + " position string: " + ladderPos);
            
            if (ladderPos != null && !ladderPos.isEmpty()) {
                String[] coords = ladderPos.split(",");
                if (coords.length == 2) {
                    try {
                        double x = Double.parseDouble(coords[0]);
                        double y = Double.parseDouble(coords[1]);
                        ladders.add(new Ladder(x, y));
                        System.out.println("DEBUG - Added ladder " + i + ": " + x + "," + y);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing ladder coordinates: " + ladderPos);
                    }
                }
            }
        }
    }
    
    /**
     * Initializes Donkey Kong from the properties file.
     *
     * @param props Properties containing DonkeyKong configuration
     */
    private void initializeDonkeyKong(Properties props) {
        String donkeyPosStr = props.getProperty("donkey.level1");
        System.out.println("DEBUG - Donkey Kong position string: " + donkeyPosStr);
        
        String[] coords = donkeyPosStr.split(",");
        if (coords.length == 2) {
            try {
                double dkX = Double.parseDouble(coords[0]);
                double dkY = Double.parseDouble(coords[1]);
        donkeyKong = new DonkeyKong(dkX, dkY);
                System.out.println("DEBUG - Set Donkey Kong position: " + dkX + "," + dkY);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing Donkey Kong coordinates: " + donkeyPosStr);
                donkeyKong = new DonkeyKong(150, 0);
            }
        }
    }
    
    /**
     * Initializes barrels from the properties file.
     * Barrel format uses "barrel.level1.count" for count and "barrel.level1.1=x,y" etc. for positions
     *
     * @param props Properties containing barrel configuration
     */
    private void initializeBarrels(Properties props) {
        int barrelCount = Integer.parseInt(props.getProperty("barrel.level1.count", "0"));
        System.out.println("DEBUG - Barrel count: " + barrelCount);
        
        for (int i = 1; i <= barrelCount; i++) {
            String barrelPos = props.getProperty("barrel.level1." + i);
            System.out.println("DEBUG - Barrel " + i + " position string: " + barrelPos);
            
            if (barrelPos != null && !barrelPos.isEmpty()) {
                String[] coords = barrelPos.split(",");
                if (coords.length == 2) {
                    try {
                        double x = Double.parseDouble(coords[0]);
                        double y = Double.parseDouble(coords[1]);
                        barrels.add(new Barrel(x, y));
                        System.out.println("DEBUG - Added barrel " + i + ": " + x + "," + y);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing barrel coordinates: " + barrelPos);
                    }
                }
            }
        }
    }
    
    /**
     * Initializes the hammer from the properties file.
     * Hammer format uses "hammer.level1.count" for count and "hammer.level1.1=x,y" for position
     *
     * @param props Properties containing hammer configuration
     */
    private void initializeHammer(Properties props) {
        int hammerCount = Integer.parseInt(props.getProperty("hammer.level1.count", "0"));
        System.out.println("DEBUG - Hammer count: " + hammerCount);
        
        if (hammerCount > 0) {
            String hammerPos = props.getProperty("hammer.level1.1");
            System.out.println("DEBUG - Hammer position string: " + hammerPos);
            
            if (hammerPos != null && !hammerPos.isEmpty()) {
                String[] coords = hammerPos.split(",");
                if (coords.length == 2) {
                    try {
                        double hammerX = Double.parseDouble(coords[0]);
                        double hammerY = Double.parseDouble(coords[1]);
        hammer = new Hammer(hammerX, hammerY);
                        System.out.println("DEBUG - Set hammer position: " + hammerX + "," + hammerY);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing hammer coordinates: " + hammerPos);
                        hammer = new Hammer(900, 500); // Default position if parsing fails
                    }
                }
            }
        } else {
            // Default position if no hammer configuration found
            hammer = new Hammer(900, 500);
        }
    }
    
    /**
     * Corrects the initial positions of all entities according to clarification #101.
     * If any entity overlaps with a platform, it is placed on top of the platform.
     */
    private void correctInitialPositions() {
        // Note: We don't force player onto platform initially
        // Player will fall naturally due to gravity until landing on a platform
        // This matches the requirement to use app.properties position
        
        // For Project 2, ladders should fall naturally due to gravity (0.25 pixels/frame)
        // so we don't place them on platforms immediately
        
        // Position barrels on platforms if they overlap
        for (Barrel barrel : barrels) {
            for (Platform platform : platforms) {
                if (platform.overlaps(barrel)) {
                    platform.placeEntityOnTop(barrel);
                    barrel.setOnGround(true);
                    break;
                }
            }
        }
        
        // Position DonkeyKong on a platform if overlapping
        for (Platform platform : platforms) {
            if (platform.overlaps(donkeyKong)) {
                platform.placeEntityOnTop(donkeyKong);
                donkeyKong.setOnGround(true);
                break;
            }
        }
        
        // Position hammer on a platform if overlapping
        for (Platform platform : platforms) {
            if (platform.overlaps(hammer)) {
                platform.placeEntityOnTop(hammer);
                break;
            }
        }
    }
    
    /**
     * Checks for collisions between the player and platforms.
     * If the player is standing on a platform, prevents falling.
     * Also handles more complex collision scenarios to prevent penetration.
     */
    private void checkPlatformCollisions() {
        // Skip platform collision check if player is on a ladder
        if (player.isOnLadder()) {
            return;
        }
        
        boolean onPlatform = false;
        Platform collidingPlatform = null;
        
        // First, check if player is on any platform
        for (Platform platform : platforms) {
            if (platform.isCollidingFromTop(player)) {
                collidingPlatform = platform;
                onPlatform = true;
                break;
            }
        }
        
        // If a collision was detected, handle it
        if (onPlatform && collidingPlatform != null) {
            collidingPlatform.placeEntityOnTop(player);
            
            // When high falling speed might cause penetration,
            // ensure player stays on top of platform
            if (player.getVerticalVelocity() > 5) {
                // Double-check position to prevent any residual penetration
                double expectedY = collidingPlatform.getY() - collidingPlatform.getHeight()/2 - player.getHeight()/2;
                if (Math.abs(player.getY() - expectedY) > 1) {
                    player.setY(expectedY);
                }
            }
        }

        // Check for head collisions with platforms (when player is jumping up)
        if (player.getVerticalVelocity() < 0) {  // Player is moving upward
            for (Platform platform : platforms) {
                // Calculate platform bottom edge
                double platformBottomY = platform.getY() + platform.getHeight()/2;
                // Calculate player top edge
                double playerTopY = player.getY() - player.getHeight()/2;
                
                // Check if player's top edge is colliding with platform's bottom edge
                // and player is within platform's horizontal bounds
                if (playerTopY <= platformBottomY && 
                    platform.isWithinHorizontalBounds(player) &&
                    player.getPreviousY() - player.getHeight()/2 > platformBottomY) {
                    
                    // Stop upward movement and position player just below platform
                    player.setY(platformBottomY + player.getHeight()/2 + 1);
                    player.setVerticalVelocity(0);
                    break;
                }
            }
        }
        
        // Update player's ground state
        player.setOnGround(onPlatform);
    }

    /**
     * Checks for collisions between the ladders and platforms.
     * Ensures ladders stay on platforms and don't fall through.
     */
    private void checkLadderPlatformCollisions() {
        for (Ladder ladder : ladders) {
            boolean onPlatform = false;
            
            for (Platform platform : platforms) {
                if (platform.isCollidingFromTop(ladder)) {
                    // Only place on top if ladder is falling (positive velocity)
                    if (ladder.getVerticalVelocity() > 0) {
                        platform.placeEntityOnTop(ladder);
                    }
                    ladder.setOnGround(true);
                    onPlatform = true;
                    break;
                }
            }
            
            ladder.setOnGround(onPlatform);
        }
    }
    
    /**
     * Checks for interactions between the player and ladders.
     * Handles starting/stopping climbing and movement on ladders.
     *
     * @param input Current input state
     */
    private void checkPlayerLadderInteractions(Input input) {
        // If player is not currently on a ladder, check if they can start climbing
        if (!player.isOnLadder()) {
            for (Ladder ladder : ladders) {
                // Check if player is positioned to climb this ladder
                if (ladder.canPlayerClimb(player)) {
                    if (input.isDown(Keys.UP) || input.isDown(Keys.DOWN)) {
                        player.startClimbing(ladder);
                        return;
                    }
                }
                
                // Special case: Check if player is at the top of a ladder and pressing DOWN
                if (ladder.isPlayerAtTopOfLadder(player) && input.isDown(Keys.DOWN)) {
                    player.startClimbing(ladder);
                    return;
                }
                
                // New case: Check if player is standing on a platform above a ladder and wants to climb down
                if (input.isDown(Keys.DOWN)) {
                    double ladderTopY = ladder.getY() - ladder.getHeight()/2;
                    double playerBottomY = player.getY() + player.getHeight()/2;
                    
                    // Check if player is directly above the ladder and on ground (platform)
                    if (Math.abs(player.getX() - ladder.getX()) < 10 && 
                        Math.abs(playerBottomY - ladderTopY) < 5 &&
                        player.isOnGround()) {
                        
                        player.startClimbing(ladder);
                        player.setY(ladderTopY - player.getHeight()/2);
                        return;
                    }
                }
            }
        } 
        // If player is on a ladder, check if they should stop climbing
        else {
            Ladder currentLadder = player.getCurrentLadder();
            if (currentLadder == null) {
                player.stopClimbing();
                return;
            }
            
            // Get ladder bounds
            double ladderTopY = currentLadder.getY() - currentLadder.getHeight()/2;
            double ladderBottomY = currentLadder.getY() + currentLadder.getHeight()/2;
            double ladderLeftEdge = currentLadder.getX() - currentLadder.getWidth()/2;
            double ladderRightEdge = currentLadder.getX() + currentLadder.getWidth()/2;
            
            // Get player bounds
            double playerTopY = player.getY() - player.getHeight()/2;
            double playerBottomY = player.getY() + player.getHeight()/2;
            
            // Handle player reaching top of ladder - check if we should continue climbing beyond top
            if (playerTopY < ladderTopY) {
                // Check if there's a platform at the top that player could climb onto
                boolean platformAbove = false;
                for (Platform platform : platforms) {
                    double platformTopY = platform.getY() - platform.getHeight()/2;
                    // Platform must be close to the ladder top and player must be horizontally aligned
                    if (Math.abs(platformTopY - ladderTopY) < 10 && 
                        platform.isWithinHorizontalBounds(player)) {
                        platformAbove = true;
                        // Only stop climbing if player is high enough to stand on platform
                        if (playerBottomY <= platformTopY + 5) {
                            // Check if player wants to go down the ladder
                            if (input.isDown(Keys.DOWN)) {
                                // Allow player to descend the ladder - don't reposition, just stay in climbing mode
                                return;
                            }
                            
                            player.stopClimbing();
                            // Help player land on platform by positioning correctly
                            player.setY(platformTopY - player.getHeight()/2 - 1);
                            player.setOnGround(true);
                            return;
                        }
                        break;
                    }
                }
                
                // If no platform or not high enough, limit upward movement
                if (!platformAbove) {
                    // Prevent climbing past ladder top when no platform exists
                    player.setY(ladderTopY + player.getHeight()/2);
                    player.setVerticalVelocity(0);
                }
            }
            
            // Handle player at bottom of ladder
            if (playerBottomY > ladderBottomY) {
                // Check if there's a platform at the bottom of the ladder
                for (Platform platform : platforms) {
                    double platformTopY = platform.getY() - platform.getHeight()/2;
                    // If platform is directly beneath ladder and player is on it
                    if (Math.abs(platformTopY - ladderBottomY) < 5 &&
                        platform.isWithinHorizontalBounds(player) &&
                        Math.abs(playerBottomY - platformTopY) < 5) {
                        // Exit ladder mode and stand on platform
                        player.stopClimbing();
                        return;
                    }
                }
                
                // No platform directly below, prevent falling through by limiting position
                if (input.isDown(Keys.DOWN)) {
                    // Don't reset position on every frame to avoid jitter - just limit excursion
                    double maxAllowedY = ladderBottomY - player.getHeight()/2 + 2;
                    if (player.getY() > maxAllowedY) {
                        player.setY(maxAllowedY);
                    }
                }
            }
            
            // Check if player has moved off the ladder horizontally
            if (player.getX() < ladderLeftEdge - 5 || player.getX() > ladderRightEdge + 5) {
                player.stopClimbing();
                return;
            }
        }
    }

    /**
     * Checks for collisions between barrels and platforms.
     * Ensures barrels stay on platforms and don't fall.
     */
    private void checkBarrelPlatformCollisions() {
        for (Barrel barrel : barrels) {
            if (barrel.isDestroyed()) {
                continue; // Skip destroyed barrels
            }
            
            boolean onPlatform = false;
            
            for (Platform platform : platforms) {
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
     * Checks for collisions between DonkeyKong and platforms.
     * Ensures DonkeyKong stays on platforms and doesn't fall.
     */
    private void checkDonkeyKongPlatformCollisions() {
        boolean onPlatform = false;
        
        for (Platform platform : platforms) {
            if (platform.isCollidingFromTop(donkeyKong)) {
                platform.placeEntityOnTop(donkeyKong);
                donkeyKong.setOnGround(true);
                onPlatform = true;
                break;
            }
        }
        
        donkeyKong.setOnGround(onPlatform);
    }
    
    /**
     * Updates the game state based on input and elapsed time.
     * This is called each frame.
     *
     * @param input The current input state
     * @return The new game state, or null to stay in current state
     */
    @Override
    public GameState update(Input input) {
        // Exit the game if Escape is pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        
        // Update game timer
        currentFrame++;
        if (currentFrame >= maxFrames) {
            // Time's up - game over
            scoreManager.addTimeBonus(0); // No time bonus when time runs out
            return GameState.GAME_OVER_LOSE;
        }
        
        // Update player position based on input
        player.handleInput(input, windowWidth, windowHeight);
        
        // Update entities
        player.update();
        donkeyKong.update();
        
        // Update ladders
        for (Ladder ladder : ladders) {
            ladder.update();
        }
        
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
        
        // Check for platform collisions
        checkPlatformCollisions();
        
        // Check for ladder-platform interactions
        checkLadderPlatformCollisions();
        
        // Check for player-ladder interactions
        checkPlayerLadderInteractions(input);
        
        // Check for barrel-platform interactions
        checkBarrelPlatformCollisions();
        
        // Check for Donkey Kong platform interactions
        checkDonkeyKongPlatformCollisions();
        
        // Check for game-ending collisions
        GameState collisionResult = checkPlayerCollisions();
        if (collisionResult != null) {
            // Add time bonus before returning game over state
            int remainingSeconds = (maxFrames - currentFrame) / 60;
            scoreManager.addTimeBonus(remainingSeconds);
            return collisionResult;
        }
        
        // Continue in gameplay state
        return null;
    }
    
    /**
     * Checks for collisions between the player and hammer.
     * If the player touches the hammer, they collect it.
     */
    private void checkHammerCollision() {
        if (!hammer.isCollected() && player.getBoundingBox().intersects(hammer.getBoundingBox())) {
            // Player collected the hammer
            hammer.collect();
            player.collectHammer();
        }
    }
    
    /**
     * Checks if the player jumps over any barrels.
     * Awards points for successful jumps.
     * Improved logic to prevent multiple scores from a single jump.
     */
    private void checkBarrelJumps() {
        double playerY = player.getY();
        double playerX = player.getX();
        
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
            boolean isCloseHorizontally = Math.abs(playerX - barrelX) < (player.getWidth()/2 + barrel.getWidth()/2 + 5);
            
            // Determine which side of the barrel the player is on
            int currentSide = 0;
            if (Math.abs(playerX - barrelX) > 5) { // Only if not directly above/below
                currentSide = (playerX > barrelX) ? 1 : -1;
            }
            
            // Player directly above barrel & falling
            boolean playerAboveBarrel = playerY < barrelTopY && player.getVerticalVelocity() > 0;
            
            // Reset status when player is on ground
            if (player.isOnGround()) {
                // Record which side of barrel player is on when grounded
                if (!isCloseHorizontally && currentSide != 0) {
                    jumpInfo.side = currentSide;
                }
                // Reset jump tracking
                if (jumpInfo.isAboveBarrel) {
                    jumpInfo.isAboveBarrel = false;
                    jumpInfo.hasJumpedOver = false;
                }
            }
            
            // Start tracking a potential jump
            if (playerAboveBarrel && isCloseHorizontally && player.getVerticalVelocity() > 0) {
                jumpInfo.isAboveBarrel = true;
                jumpInfo.lastPlayerY = playerY;
            }
            
            // Check if player has moved downward since we started tracking
            boolean movingDownward = playerY > jumpInfo.lastPlayerY;
            jumpInfo.lastPlayerY = playerY;
            
            // Detect completed jump - player was above barrel, is now down and on opposite side
            if (jumpInfo.isAboveBarrel && movingDownward && !jumpInfo.hasJumpedOver && 
                jumpInfo.cooldownTimer == 0 && currentSide != 0 && jumpInfo.side != 0 && 
                currentSide != jumpInfo.side) {
                
                // Award points
                scoreManager.addScore(30);
                
                // Mark as jumped and start cooldown
                jumpInfo.hasJumpedOver = true;
                jumpInfo.cooldownTimer = JUMP_COOLDOWN;
            }
        }
        
        // Clean up destroyed barrels
        barrels.stream()
            .filter(Barrel::isDestroyed)
            .forEach(barrelJumpStatuses::remove);
    }
    
    /**
     * Checks for game-ending collisions:
     * - Player touching DonkeyKong without a hammer (game over, lose)
     * - Player touching DonkeyKong with a hammer (game over, win)
     * - Player touching a barrel without a hammer (game over, lose)
     * - Player touching a barrel with a hammer (destroy barrel, +100 points)
     *
     * @return The new game state if collision causes game end, null otherwise
     */
    private GameState checkPlayerCollisions() {
        // Check for player-DK collision
        if (player.getBoundingBox().intersects(donkeyKong.getBoundingBox())) {
            if (player.hasHammer()) {
                // Win condition: Player touched DK with a hammer
                return GameState.GAME_OVER_WIN;
            } else {
                // Lose condition: Player touched DK without a hammer
                return GameState.GAME_OVER_LOSE;
            }
        }
        
        // Check for player-barrel collisions
        for (Barrel barrel : barrels) {
            if (player.getBoundingBox().intersects(barrel.getBoundingBox())) {
                if (player.hasHammer()) {
                    // Destroy barrel and score points
                    barrel.destroy();
                    scoreManager.addScore(100); // 100 points for destroying a barrel
                } else {
                    // Lose condition: Player touched barrel without a hammer
                    return GameState.GAME_OVER_LOSE;
                }
            }
        }
        
        // No game-ending collisions
        return null;
    }
    
    /**
     * Gets the current score.
     *
     * @return The current score
     */
    public int getScore() {
        return scoreManager.getScore();
    }
    
    /**
     * Gets the remaining time in seconds.
     *
     * @return The remaining time in seconds
     */
    public int getRemainingTime() {
        return (maxFrames - currentFrame) / 60;
    }
    
    /**
     * Draws all game elements on the screen.
     */
    @Override
    public void draw() {
        // Draw background using parent class method
        drawBackground();
        
        // Draw game entities
        for (Platform platform : platforms) {
            platform.draw();
        }
        
        for (Ladder ladder : ladders) {
            ladder.draw();
        }
        
        for (Barrel barrel : barrels) {
            if (!barrel.isDestroyed()) {
                barrel.draw();
            }
        }
        
        if (!hammer.isCollected()) {
            hammer.draw();
        }
        
        donkeyKong.draw();
        player.draw();
        
        // Draw score and time
        String scoreText = "Score: " + scoreManager.getScore();
        scoreFont.drawString(scoreText, scoreX, scoreY);
        
        // Draw time remaining
        int remainingSeconds = getRemainingTime();
        String timeText = "Time: " + remainingSeconds;
        scoreFont.drawString(timeText, scoreX, timeY);
    }
} 