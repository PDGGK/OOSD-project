package entities;

import bagel.Image;
import bagel.util.Rectangle;

/**
 * Represents a platform in the game.
 * Platforms provide surfaces for other entities to stand on.
 */
public class Platform extends Entity {
    // Platform image
    private static final Image PLATFORM_IMAGE = new Image("res/platform.png");
    
    // Collision detection tolerance (in pixels)
    private static final double COLLISION_TOLERANCE = 5.0;

    /**
     * Creates a new platform at the specified position.
     * Note: (x,y) represents the center of the platform.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     */
    public Platform(double x, double y) {
        super(x, y, PLATFORM_IMAGE);
    }

    /**
     * Platforms don't have dynamic behavior, so this method is empty.
     */
    @Override
    public void update() {
        // Platforms do not move or change state
    }

    /**
     * Checks if an entity is colliding with the top of this platform.
     * Used to determine if an entity should land on this platform.
     *
     * @param entity The entity to check for collision
     * @return true if the entity is colliding with the top of this platform
     */
    public boolean isCollidingFromTop(Entity entity) {
        // Get the bottom center point of the entity
        double entityBottomY = entity.getY() + entity.getHeight()/2;
        double entityCenterX = entity.getX();
        
        // For Player entities, use previous position to detect collisions
        double previousBottomY = entityBottomY;
        double verticalVelocity = 0;
        if (entity instanceof Player) {
            Player player = (Player) entity;
            previousBottomY = player.getPreviousY() + player.getHeight()/2;
            verticalVelocity = player.getVerticalVelocity();
        }
        
        // Check if entity's bottom center is within platform's horizontal bounds
        boolean isWithinHorizontalBounds = entityCenterX >= this.getX() - this.getWidth()/2 && 
                                           entityCenterX <= this.getX() + this.getWidth()/2;
                                         
        // The top edge of the platform is at y - height/2
        double platformTopY = this.getY() - this.getHeight()/2;
        
        // If player is in early jump phase (negative velocity), don't detect collision
        // This prevents immediate collision detection right after jumping
        if (entity instanceof Player && verticalVelocity < 0) {
            // Only check for penetration, not top edge contact during jump
            boolean hasPenetrated = previousBottomY <= platformTopY && 
                                    entityBottomY > platformTopY;
            return isWithinHorizontalBounds && hasPenetrated;
        } else {
            // Normal collision check for non-jumping entities or falling players
            // Check if entity's bottom is at or slightly above platform's top,
            // or if it was above but has now passed through (penetration detection)
            boolean isAtTopEdge = entityBottomY >= platformTopY - COLLISION_TOLERANCE && 
                                  entityBottomY <= platformTopY + COLLISION_TOLERANCE;
            
            // Check for penetration - entity was above platform in previous frame
            // but now is below the top of the platform
            boolean hasPenetrated = previousBottomY <= platformTopY && 
                                    entityBottomY > platformTopY;
                                 
            return isWithinHorizontalBounds && (isAtTopEdge || hasPenetrated);
        }
    }
    
    /**
     * Checks if an entity is within the horizontal bounds of this platform.
     * Used for entity-platform alignment checks.
     *
     * @param entity The entity to check
     * @return true if the entity is within the horizontal bounds of the platform
     */
    public boolean isWithinHorizontalBounds(Entity entity) {
        // Get entity center X
        double entityCenterX = entity.getX();
        
        // Check if entity center is within platform's horizontal bounds
        return entityCenterX >= this.getX() - this.getWidth()/2 && 
               entityCenterX <= this.getX() + this.getWidth()/2;
    }
    
    /**
     * Checks if any part of an entity overlaps with any part of this platform.
     * Used for initial placement correction as per clarification #101.
     *
     * @param entity The entity to check for overlap
     * @return true if the entity overlaps with this platform
     */
    public boolean overlaps(Entity entity) {
        Rectangle entityBounds = entity.getBoundingBox();
        Rectangle platformBounds = this.getBoundingBox();
        
        return platformBounds.intersects(entityBounds);
    }
    
    /**
     * Places an entity directly on top of this platform.
     * Used for initial placement and landing mechanics.
     *
     * @param entity The entity to place on top of the platform
     */
    public void placeEntityOnTop(Entity entity) {
        // Calculate the top edge of the platform (center y - half height)
        double platformTopY = this.getY() - this.getHeight()/2;
        
        // Set entity's Y position so its bottom edge aligns with platform's top edge
        // Entity bottom = entity.y + entity.height/2
        // We want: entity.y + entity.height/2 = platformTopY
        // So entity.y = platformTopY - entity.height/2
        entity.setY(platformTopY - entity.getHeight()/2);
        
        // If entity is a player, reset its vertical velocity
        if (entity instanceof Player) {
            ((Player) entity).setVerticalVelocity(0);
        }
    }
} 