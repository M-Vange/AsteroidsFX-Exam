package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Random;

public class AsteroidProcessor implements IEntityProcessingService {

    // Class handling the rotation and movement of asteroids.
    // Also handles screen wrapping logic.

    private final Random rnd = new Random();

    // Timer variable. 60 frames = 1 second. First asteroid spawns after 3 seconds.
    private int spawnTimer = 180;

    // Asteroid limit for spawning new ones.
    // Asteroids splitting won't cause asteroids to despawn, but will count towards the limit.
    // This stops new asteroids from spawning, regardless of the asteroid size.
    private final int maxAsteroids = 6;


    @Override
    public void process(GameData gameData, World world) {

        // Continuous spawning logic. Timer runs 60 times a second. Subtracting 1 every frame.
        spawnTimer--;

        if (spawnTimer <= 0) {
            // Counting how many asteroids exist in the world.
            int currentAsteroidCount = world.getEntities(Asteroid.class).size();

            //  Spawns new asteroid if amount is less than asteroid limit.
            if (currentAsteroidCount < maxAsteroids) {
                Entity newAsteroid = createAsteroidAtEdge(gameData);
                world.addEntity(newAsteroid);
            }

            // Reset timer. Set new random spawn timer between 2 seconds (120 frames) and 5 seconds (300 frames).
            // rnd.nextInt(181) gives a number between 0 and 180.
            spawnTimer = 120 + rnd.nextInt(181);
        }


        for (Entity entity : world.getEntities(Asteroid.class)) {
            // Getting the special properties from Asteroid class
            Asteroid asteroid = (Asteroid) entity;

            // Move asteroid in its dedicated direction
            double changeX = Math.cos(Math.toRadians(asteroid.getMoveAngle())) * asteroid.getSpeed();
            double changeY = Math.sin(Math.toRadians(asteroid.getMoveAngle())) * asteroid.getSpeed();
            asteroid.setX(asteroid.getX() + changeX);
            asteroid.setY(asteroid.getY() + changeY);

            // Making asteroid spin visually
            asteroid.setRotation(asteroid.getRotation() + asteroid.getRotationSpeed());

            // Fixed screen-wrapping logic
            if (asteroid.getX() < 0) {
                asteroid.setX(asteroid.getX() + gameData.getDisplayWidth());
            }
            else if (asteroid.getX() > gameData.getDisplayWidth()) {
                asteroid.setX(asteroid.getX() - gameData.getDisplayWidth());
            }

            if (asteroid.getY() < 0) {
                asteroid.setY(asteroid.getY() + gameData.getDisplayHeight());
            }
            else if (asteroid.getY() > gameData.getDisplayHeight()) {
                asteroid.setY(asteroid.getY() - gameData.getDisplayHeight());
            }
        }
    }

    // Helper method to spawn new asteroids at edges.
    // Copy of createAsteroid from AsteroidPlugin.
    private Entity createAsteroidAtEdge(GameData gameData) {
        double randomSpeed = 0.5 + (rnd.nextDouble() * 0.5);
        double randomMoveAngle = rnd.nextInt(360);
        double randomSpin = 0.5 + (rnd.nextDouble() * 1.5);

        Asteroid asteroid = new Asteroid(randomSpeed, randomMoveAngle, randomSpin);

        // Picking a random border edge for spawn. 0=Top, 1=Bottom, 2=Left, 3=Right
        int side = rnd.nextInt(4);
        int width = gameData.getDisplayWidth();
        int height = gameData.getDisplayHeight();

        if (side == 0) {
            asteroid.setX(rnd.nextInt(width));
            asteroid.setY(0);
        } else if (side == 1) {
            asteroid.setX(rnd.nextInt(width));
            asteroid.setY(height);
        } else if (side == 2) {
            asteroid.setX(0);
            asteroid.setY(rnd.nextInt(height));
        } else {
            asteroid.setX(width);
            asteroid.setY(rnd.nextInt(height));
        }

        int baseRadius = rnd.nextInt(15) + 15;
        asteroid.setRadius(baseRadius);

        int points = 7;
        double[] coordinates = new double[points * 2];
        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            double bumpyRadius = baseRadius + (rnd.nextInt(11) - 5);
            coordinates[i * 2] = Math.cos(angle) * bumpyRadius;
            coordinates[i * 2 + 1] = Math.sin(angle) * bumpyRadius;
        }

        asteroid.setPolygonCoordinates(coordinates);
        return asteroid;
    }
}
