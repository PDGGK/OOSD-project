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
import interfaces.Collidable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Abstract base class for gameplay screens.
 * Provides common functionality for different game levels.
 * Each level should extend this class and implement level-specific behavior.
 */
public abstract class GameplayScreen extends Screen {
    // Collision Detection Constants
    private static final double PLATFORM_LADDER_ALIGNMENT_TOLERANCE = 10.0; // Platform-ladder alignment tolerance
    private static final double PLATFORM_PLAYER_COLLISION_TOLERANCE = 5.0; // Player-platform collision tolerance
    private static final double LADDER_EXIT_TOLERANCE = 5.0; // Horizontal distance for exiting ladder
    private static final double PLAYER_PLATFORM_PLACEMENT_OFFSET = 1.0; // Offset when placing player on platform
    
    // Time Calculation Constants
    private static final int FRAMES_PER_SECOND = 60; // Frames per second for time calculation
    
    // Debug flag
    private static final boolean DEBUG = false;
    
    // Common game elements that all levels have
    private final Player player;
    private final int windowWidth;
    private final int windowHeight;
    
    private final Font scoreFont;
    private final int scoreX;
    private final int scoreY;
    private final int timeY;
    
    private int currentFrame = 0;
    private final int maxFrames;
    
    // Game elements common to all levels
    private final List<Platform> platforms = new ArrayList<>();
    private final List<Ladder> ladders = new ArrayList<>();
    private DonkeyKong donkeyKong;
    
    // Scoring system
    private final ScoreManager scoreManager;
    
    /**
     * Creates a new gameplay screen.
     *
     * @param gameProps Properties containing game configuration
     * @param messageProps Properties containing game messages
     */
    public GameplayScreen(Properties gameProps, Properties messageProps) {
        super(gameProps, messageProps);
        
        this.windowWidth = Window.getWidth();
        this.windowHeight = Window.getHeight();
        
        // Load fonts and positions from properties
        this.scoreFont = createFont("gamePlay.score.fontSize");
        this.scoreX = Integer.parseInt(gameProps.getProperty("gamePlay.score.x"));
        this.scoreY = Integer.parseInt(gameProps.getProperty("gamePlay.score.y"));
        timeY = scoreY + 30; // 30 pixels below score display
        
        // Load max frames for timing
        this.maxFrames = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames"));
        
        // Initialize score manager
        this.scoreManager = new ScoreManager();
        
        // Load player
        String playerKey = getPlayerPropertyKey();
        String[] playerCoords = gameProps.getProperty(playerKey).split(",");
        double playerX = Double.parseDouble(playerCoords[0]);
        double playerY = Double.parseDouble(playerCoords[1]);
        this.player = new Player(playerX, playerY);
        
        // Load platforms
        initializePlatforms(gameProps);
        
        // Load ladders
        initializeLadders(gameProps);
        
        // Load Donkey Kong
        initializeDonkeyKong(gameProps);
        
        // Note: loadLevelEntities() is called by init() after subclass construction
        // to ensure subclass fields are properly initialized
    }
    
    /**
     * Initialize the gameplay screen.
     * This should be called after the constructor completes.
     * It loads level-specific entities and corrects positions.
     */
    public void init() {
        // Load level-specific entities
        loadLevelEntities();
        
        // Initial position correction for all entities
        correctInitialPositions();
    }
    
    /**
     * Gets the property key for player position.
     * Should be overridden by subclasses to return level-specific key.
     * 
     * @return Property key for player position
     */
    protected abstract String getPlayerPropertyKey();
    
    /**
     * Gets the property key for platforms.
     * Should be overridden by subclasses to return level-specific key.
     * 
     * @return Property key for platforms
     */
    protected abstract String getPlatformsPropertyKey();
    
    /**
     * Gets the property key for ladders count.
     * Should be overridden by subclasses to return level-specific key.
     * 
     * @return Property key for ladders count
     */
    protected abstract String getLaddersCountPropertyKey();
    
    /**
     * Gets the property key format for ladder position.
     * Should be overridden by subclasses to return level-specific key format.
     * 
     * @return Property key format for ladder position
     */
    protected abstract String getLadderPropertyKeyFormat();
    
    /**
     * Gets the property key for Donkey Kong position.
     * Should be overridden by subclasses to return level-specific key.
     * 
     * @return Property key for Donkey Kong position
     */
    protected abstract String getDonkeyKongPropertyKey();
    
    /**
     * Loads level-specific entities (barrels, hammer, monkeys, etc).
     * Must be implemented by subclasses.
     */
    protected abstract void loadLevelEntities();
    
    /**
     * Processes level-specific updates.
     * Must be implemented by subclasses.
     * 
     * @param input User input
     * @return GameState change if any, or null to continue
     */
    protected abstract GameState updateLevelSpecific(Input input);
    
    /**
     * Checks for level-specific collisions.
     * Must be implemented by subclasses.
     * 
     * @return GameState change if any, or null to continue
     */
    protected abstract GameState checkLevelSpecificCollisions();
    
    /**
     * Draws level-specific entities and UI elements.
     * Must be implemented by subclasses.
     */
    protected abstract void drawLevelSpecific();
    
    /**
     * Initializes platforms from the properties file.
     * Platform data is in the format "x1,y1;x2,y2;x3,y3;..."
     *
     * @param props Properties containing platform configuration
     */
    protected void initializePlatforms(Properties props) {
        String platformsStr = props.getProperty(getPlatformsPropertyKey());
        
        if (DEBUG) {
        System.out.println("DEBUG - Platform string: " + platformsStr);
        }
        
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
                        
                        if (DEBUG) {
                        System.out.println("DEBUG - Added platform: " + x + "," + y);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing platform coordinates: " + platform);
                    }
                }
            }
        }
    }
    
    /**
     * Initializes ladders from the properties file.
     * Ladder format uses "ladder.levelX.count" for count and "ladder.levelX.1=x,y" etc. for positions
     *
     * @param props Properties containing ladder configuration
     */
    protected void initializeLadders(Properties props) {
        int ladderCount = Integer.parseInt(props.getProperty(getLaddersCountPropertyKey(), "0"));
        
        if (DEBUG) {
        System.out.println("DEBUG - Ladder count: " + ladderCount);
        }
        
        for (int i = 1; i <= ladderCount; i++) {
            String ladderKey = String.format(getLadderPropertyKeyFormat(), i);
            String ladderPos = props.getProperty(ladderKey);
            
            if (DEBUG) {
            System.out.println("DEBUG - Ladder " + i + " position string: " + ladderPos);
            }
            
            if (ladderPos != null && !ladderPos.isEmpty()) {
                String[] coords = ladderPos.split(",");
                if (coords.length == 2) {
                    try {
                        double x = Double.parseDouble(coords[0]);
                        double y = Double.parseDouble(coords[1]);
                        ladders.add(new Ladder(x, y));
                        
                        if (DEBUG) {
                        System.out.println("DEBUG - Added ladder " + i + ": " + x + "," + y);
                        }
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
    protected void initializeDonkeyKong(Properties props) {
        String donkeyPosStr = props.getProperty(getDonkeyKongPropertyKey());
        
        if (DEBUG) {
        System.out.println("DEBUG - Donkey Kong position string: " + donkeyPosStr);
        }
        
        String[] coords = donkeyPosStr.split(",");
        if (coords.length == 2) {
            try {
                double dkX = Double.parseDouble(coords[0]);
                double dkY = Double.parseDouble(coords[1]);
        donkeyKong = new DonkeyKong(dkX, dkY);
                
                if (DEBUG) {
                System.out.println("DEBUG - Set Donkey Kong position: " + dkX + "," + dkY);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing Donkey Kong coordinates: " + donkeyPosStr);
                donkeyKong = new DonkeyKong(150, 0);
            }
        }
    }
    
    /**
     * Corrects entity positions to ensure they don't overlap platforms.
     * Objects like ladders, barrels, and Donkey Kong should be placed on top
     * of platforms if they are partially overlapping.
     */
    protected void correctInitialPositions() {
        // Correct player position if overlapping with a platform
            for (Platform platform : platforms) {
            if (platform.overlaps(player)) {
                platform.placeEntityOnTop(player);
                player.setOnGround(true);
                    break;
            }
        }
        
        // Correct Donkey Kong position if overlapping with a platform
        for (Platform platform : platforms) {
            if (platform.overlaps(donkeyKong)) {
                platform.placeEntityOnTop(donkeyKong);
                donkeyKong.setOnGround(true);
                break;
            }
        }
        
        // For Project 2, ladders should fall naturally due to gravity (0.25 pixels/frame)
        // We no longer force them onto platforms at initialization
        // They will naturally land on platforms due to their physics
        
        // Call subclass-specific position corrections
        correctLevelSpecificPositions();
    }
    
    /**
     * Corrects positions of level-specific entities.
     * Should be implemented by subclasses to handle their unique entities.
     */
    protected abstract void correctLevelSpecificPositions();
    
    /**
     * Checks for player-ladder interactions.
     * Handles player climbing behavior.
     *
     * @param input User input to process climbing actions
     */
    protected void checkPlayerLadderInteractions(Input input) {
        // If player is not currently on a ladder, check if they can start climbing
        if (!player.isOnLadder()) {
            // Check if player wants to climb down a ladder from a platform
            if (player.isOnGround() && input.isDown(Keys.DOWN)) {
        for (Ladder ladder : ladders) {
                    // Check if player is positioned to climb down this ladder
                    if (ladder.isPlayerAtTopOfLadder(player)) {
                        player.startClimbing(ladder);
                        return;
                }
        }
    }
    
            // Check if player wants to start climbing a ladder (up or already on it)
            for (Ladder ladder : ladders) {
                if (ladder.canPlayerClimb(player)) {
                    if (input.isDown(Keys.UP) || input.isDown(Keys.DOWN)) {
                        player.startClimbing(ladder);
                        return;
                    }
                }
            }
                }
        // If player is already on a ladder, check if they should stop climbing
        else {
            Ladder currentLadder = player.getCurrentLadder();
            if (currentLadder == null) {
                player.stopClimbing();
                return;
            }
            
            // Get ladder bounds
            double ladderTopY = currentLadder.getY() - currentLadder.getHeight()/2;
            double ladderBottomY = currentLadder.getY() + currentLadder.getHeight()/2;
            
            // Get player bounds
            double playerTopY = player.getY() - player.getHeight()/2;
            double playerBottomY = player.getY() + player.getHeight()/2;
            
            // Handle player reaching top of ladder - check if there's a platform
            if (playerTopY < ladderTopY) {
                // Check if there's a platform at the top that player could climb onto
                boolean platformAbove = false;
                for (Platform platform : platforms) {
                    double platformTopY = platform.getY() - platform.getHeight()/2;
                    // Platform must be close to the ladder top and player must be horizontally aligned
                    if (Math.abs(platformTopY - ladderTopY) < PLATFORM_LADDER_ALIGNMENT_TOLERANCE && 
                        platform.isWithinHorizontalBounds(player)) {
                        platformAbove = true;
                        // Only stop climbing if player is high enough to stand on platform
                        if (playerBottomY <= platformTopY + PLAYER_PLATFORM_PLACEMENT_OFFSET) {
                            // Check if player wants to go down the ladder
                            if (input.isDown(Keys.DOWN)) {
                                // Allow player to descend the ladder - don't reposition, just stay in climbing mode
                                return;
                            }
                            
                            player.stopClimbing();
                            // Help player land on platform by positioning correctly
                            player.setY(platformTopY - player.getHeight()/2 - PLAYER_PLATFORM_PLACEMENT_OFFSET);
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
                }
            }
            
            // Handle player at bottom of ladder
            if (playerBottomY > ladderBottomY + PLAYER_PLATFORM_PLACEMENT_OFFSET) {
                // Check if there's a platform at the bottom of the ladder
                for (Platform platform : platforms) {
                    double platformTopY = platform.getY() - platform.getHeight()/2;
                    // If platform is directly beneath ladder and player is on it
                    if (Math.abs(platformTopY - ladderBottomY) < PLATFORM_PLAYER_COLLISION_TOLERANCE &&
                        platform.isWithinHorizontalBounds(player)) {
                        // Exit ladder mode and stand on platform
                        player.stopClimbing();
                        platform.placeEntityOnTop(player);
                        player.setOnGround(true);
                        return;
                    }
                }
                
                // No platform directly below, prevent falling through by limiting position
                if (input.isDown(Keys.DOWN)) {
                    // Don't reset position on every frame to avoid jitter - just limit excursion
                    player.setY(ladderBottomY - player.getHeight()/2);
                }
            }
            
            // Check if player has moved horizontally off the ladder
            double playerCenterX = player.getX();
            double ladderLeftEdge = currentLadder.getX() - currentLadder.getWidth()/2;
            double ladderRightEdge = currentLadder.getX() + currentLadder.getWidth()/2;
            
            if (playerCenterX < ladderLeftEdge - LADDER_EXIT_TOLERANCE || playerCenterX > ladderRightEdge + LADDER_EXIT_TOLERANCE) {
                player.stopClimbing();
            }
        }
    }

    /**
     * Checks for collisions between the player and platforms.
     * Handles player landing on platforms.
     */
    protected void checkPlayerPlatformCollisions() {
        boolean onAnyPlatform = false;
        
        // Check if player is on any platform
        for (Platform platform : platforms) {
            // Skip platform checks if the player is currently climbing a ladder
            if (player.isOnLadder()) {
                continue;
            }
            
            // Check if player is colliding with the top of the platform
            if (platform.isCollidingFromTop(player)) {
                onAnyPlatform = true;
                if (!player.isOnGround()) {
                    platform.placeEntityOnTop(player);
                    player.setOnGround(true);
                }
            }
        }
        
        // If player is not on any platform and not on a ladder, they should be falling
        if (!onAnyPlatform && !player.isOnLadder()) {
            player.setOnGround(false);
        }
    }
    
    /**
     * Checks for collisions between Donkey Kong and platforms.
     * Handles Donkey Kong landing on platforms.
     */
    protected void checkDonkeyKongPlatformCollisions() {
        boolean onAnyPlatform = false;
        
        for (Platform platform : platforms) {
            if (platform.isCollidingFromTop(donkeyKong)) {
                onAnyPlatform = true;
                if (!donkeyKong.isOnGround()) {
                platform.placeEntityOnTop(donkeyKong);
                donkeyKong.setOnGround(true);
                }
            }
        }
        
        if (!onAnyPlatform) {
            donkeyKong.setOnGround(false);
        }
    }
    
    /**
     * Checks for collisions between ladders and platforms.
     * Ensures ladders stay on platforms and don't fall through.
     */
    protected void checkLadderPlatformCollisions() {
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
     * Updates the gameplay screen based on user input.
     * Handles player movement, entity updates, and collision detection.
     *
     * @param input Current input state
     * @return Next GameState if state change is needed, null otherwise
     */
    @Override
    public GameState update(Input input) {
        // Increment frame counter
        currentFrame++;
        
        // Check for game timeout
        if (currentFrame >= maxFrames) {
            // Game over due to timeout
            return GameState.GAME_OVER_LOSE;
        }
        
        // Handle player input
        player.handleInput(input, windowWidth, windowHeight);
        
        // Update entities
        player.update();
        donkeyKong.update();
        
        // Update ladders first
        for (Ladder ladder : ladders) {
            ladder.update();
        }
        
        // Then check for collisions
        checkLadderPlatformCollisions();
        checkPlayerPlatformCollisions();
        checkDonkeyKongPlatformCollisions();
        checkPlayerLadderInteractions(input);
        
        // Check for level-specific collisions that may end the game
        GameState collisionResult = checkLevelSpecificCollisions();
        if (collisionResult != null) {
            return collisionResult;
        }
        
        // Process level-specific updates
        return updateLevelSpecific(input);
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
     * Gets the current frame number.
     *
     * @return Current frame number
     */
    public int getCurrentFrame() {
        return currentFrame;
    }
    
    /**
     * Gets the maximum frames for the level.
     *
     * @return Maximum frames
     */
    public int getMaxFrames() {
        return maxFrames;
    }
    
    /**
     * Gets the player instance.
     *
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Gets the list of platforms.
     *
     * @return List of platforms
     */
    public List<Platform> getPlatforms() {
        return platforms;
    }
    
    /**
     * Gets the list of ladders.
     *
     * @return List of ladders
     */
    public List<Ladder> getLadders() {
        return ladders;
    }
    
    /**
     * Gets the Donkey Kong instance.
     *
     * @return Donkey Kong
     */
    public DonkeyKong getDonkeyKong() {
        return donkeyKong;
    }
    
    /**
     * Sets the Donkey Kong instance.
     *
     * @param donkeyKong The new Donkey Kong instance
     */
    public void setDonkeyKong(DonkeyKong donkeyKong) {
        this.donkeyKong = donkeyKong;
    }
    
    /**
     * Gets the score manager.
     *
     * @return The score manager
     */
    public ScoreManager getScoreManager() {
        return scoreManager;
    }
    
    /**
     * Gets the score font.
     *
     * @return The score font
     */
    public Font getScoreFont() {
        return scoreFont;
    }
    
    /**
     * Gets the window width.
     *
     * @return Window width
     */
    public int getWindowWidth() {
        return windowWidth;
    }
    
    /**
     * Gets the window height.
     *
     * @return Window height
     */
    public int getWindowHeight() {
        return windowHeight;
    }
    
    /**
     * Increments the current frame counter.
     */
    public void incrementFrame() {
        currentFrame++;
    }
    
    /**
     * Calculates and returns the remaining time in seconds.
     *
     * @return Remaining time in seconds
     */
    public int getRemainingTime() {
        return (maxFrames - currentFrame) / FRAMES_PER_SECOND;
    }
    
    /**
     * Gets the debug flag value.
     *
     * @return Debug flag
     */
    public static boolean isDebug() {
        return DEBUG;
    }
    
    /**
     * Draws the gameplay screen.
     * Renders all game entities and UI elements.
     */
    @Override
    public void draw() {
        // Draw background
        drawBackgroundImage();
        
        // Draw platforms
        for (Platform platform : platforms) {
            platform.draw();
        }
        
        // Draw ladders
        for (Ladder ladder : ladders) {
            ladder.draw();
        }
        
        // Draw Donkey Kong
        donkeyKong.draw();
        
        // Draw level-specific elements
        drawLevelSpecific();
        
        // Draw player (last so it appears on top)
        player.draw();
        
        // Draw score using coordinates from properties file
        String scoreText = "SCORE " + scoreManager.getScore();
        scoreFont.drawString(scoreText, scoreX, scoreY);
        
        // Draw remaining time below score
        int remainingSeconds = (maxFrames - currentFrame) / FRAMES_PER_SECOND;
        String timeText = "TIME " + remainingSeconds;
        scoreFont.drawString(timeText, scoreX, timeY);
    }
    
    /**
     * Generic method to initialize barrels for any level.
     * Reduces code duplication between Level1Screen and Level2Screen.
     *
     * @param levelPrefix The level prefix (e.g., "barrel.level1", "barrel.level2")
     * @param barrelsList The list to add barrels to
     */
    protected void initializeBarrelsForLevel(String levelPrefix, List<Barrel> barrelsList) {
        int barrelCount = Integer.parseInt(getGameProps().getProperty(levelPrefix + ".count", "0"));
        
        if (DEBUG) {
            System.out.println("DEBUG - " + levelPrefix + " barrel count: " + barrelCount);
        }
        
        for (int i = 1; i <= barrelCount; i++) {
            String barrelPos = getGameProps().getProperty(levelPrefix + "." + i);
            
            if (DEBUG) {
                System.out.println("DEBUG - " + levelPrefix + " barrel " + i + " position: " + barrelPos);
            }
            
            if (barrelPos != null && !barrelPos.isEmpty()) {
                String[] coords = barrelPos.split(",");
                if (coords.length == 2) {
                    try {
                        double x = Double.parseDouble(coords[0]);
                        double y = Double.parseDouble(coords[1]);
                        barrelsList.add(new Barrel(x, y));
                        
                        if (DEBUG) {
                            System.out.println("DEBUG - Added " + levelPrefix + " barrel " + i + ": " + x + "," + y);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing " + levelPrefix + " barrel coordinates: " + barrelPos);
                    }
                }
            }
        }
    }
    
    /**
     * Generic method to initialize hammer for any level.
     * Reduces code duplication between Level1Screen and Level2Screen.
     *
     * @param levelPrefix The level prefix (e.g., "hammer.level1", "hammer.level2")
     * @param defaultX Default X position if no hammer is configured
     * @param defaultY Default Y position if no hammer is configured
     * @return Initialized Hammer instance
     */
    protected Hammer initializeHammerForLevel(String levelPrefix, double defaultX, double defaultY) {
        int hammerCount = Integer.parseInt(getGameProps().getProperty(levelPrefix + ".count", "0"));
        
        if (DEBUG) {
            System.out.println("DEBUG - " + levelPrefix + " hammer count: " + hammerCount);
        }
        
        if (hammerCount > 0) {
            String hammerPos = getGameProps().getProperty(levelPrefix + ".1");
            
            if (DEBUG) {
                System.out.println("DEBUG - " + levelPrefix + " hammer position: " + hammerPos);
            }
            
            if (hammerPos != null && !hammerPos.isEmpty()) {
                String[] coords = hammerPos.split(",");
                if (coords.length == 2) {
                    try {
                        double hammerX = Double.parseDouble(coords[0]);
                        double hammerY = Double.parseDouble(coords[1]);
                        
                        if (DEBUG) {
                            System.out.println("DEBUG - Set " + levelPrefix + " hammer position: " + hammerX + "," + hammerY);
                        }
                        
                        return new Hammer(hammerX, hammerY);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing " + levelPrefix + " hammer coordinates: " + hammerPos);
                    }
                }
            }
        }
        
        // Return default hammer if no configuration found or parsing failed
        if (DEBUG) {
            System.out.println("DEBUG - Using default " + levelPrefix + " hammer position: " + defaultX + "," + defaultY);
        }
        return new Hammer(defaultX, defaultY);
    }
} 