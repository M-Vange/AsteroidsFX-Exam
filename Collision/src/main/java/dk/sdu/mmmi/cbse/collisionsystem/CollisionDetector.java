package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import java.util.ServiceLoader;

public class CollisionDetector implements IPostEntityProcessingService {

    public CollisionDetector() {
    }

    @Override
    public void process(GameData gameData, World world) {
        // Compare each entity against every other entity
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {

                // Skip if comparing an entity to itself
                if (entity1.getID().equals(entity2.getID())) {
                    continue;                    
                }

                // Checking if entities are touching using Pythagoras
                if (this.collides(entity1, entity2)) {
                    String type1 = entity1.getClass().getSimpleName().toLowerCase();
                    String type2 = entity2.getClass().getSimpleName().toLowerCase();

                    // If ships collide with asteroids, the ship should be destroyed
                    if ((type1.contains("player") || type1.contains("enemy")) && entity2 instanceof Asteroid) {
                        // Destroy ship. Only ship, not asteroid.
                        world.removeEntity(entity1);
                    }

                    // Asteroid should split when hit by bullets
                    if (type1.contains("bullet") && entity2 instanceof Asteroid) {
                        // Destroy bullet
                        world.removeEntity(entity1);

                        // Finding loaded module to split an asteroid
                        for (IAsteroidSplitter asteroidSplitter : ServiceLoader.load(IAsteroidSplitter.class)) {
                            asteroidSplitter.createSplitAsteroid(entity2, world);
                        }
                    }

                    // If player or enemy ship is hit by bullets a designated amount of times, destroy them.
                    if (type1.contains("bullet") && (type2.contains("player") || type2.contains("enemy"))) {
                        // Destroy bullet
                        world.removeEntity(entity1);

                        // Subtract 1 health from the ship that got hit
                        int remainingHealth = entity2.getHealth() - 1;
                        entity2.setHealth(remainingHealth);

                        // Change / fade enemy color as they lose health
                        if (type2.contains("enemy")){
                            if (remainingHealth == 2) {
                                entity2.setColor("#993D3D");
                            }
                            else if (remainingHealth == 1) {
                                entity2.setColor("#552B2B");
                            }
                        }

                        // Destroy ship if it's run out of health
                        if (entity2.getHealth() <= 0){
                            world.removeEntity(entity2);
                        }
                    }
                }
            }
        }

    }

    public Boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }

}
