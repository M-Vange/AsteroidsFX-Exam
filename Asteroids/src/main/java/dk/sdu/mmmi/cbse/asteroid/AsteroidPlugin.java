package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {

    private final Random rnd = new Random();

    @Override
    public void start(GameData gameData, World world) {
        // Spawn 2 random asteroids along the bounds of the window when game starts
        for (int i = 0; i < 2; i++) {
            Entity asteroid = createAsteroid(gameData);
            world.addEntity(asteroid);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove asteroid entities if plugin stops
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            world.removeEntity(asteroid);
        }
    }

    private Entity createAsteroid(GameData gameData) {
        // Randomly generate asteroid behavior values
        double randomSpeed = 0.5 + (rnd.nextDouble() * 0.5);
        double randomMoveAngle = rnd.nextInt(360);
        double randomSpin = 0.5 + (rnd.nextDouble() * 1.5);

        // Creating asteroid object
        Asteroid asteroid = new Asteroid(randomSpeed, randomMoveAngle, randomSpin);

        // Picking a random border edge for spawn. 0=Top, 1=Bottom, 2=Left, 3=Right
        int side = rnd.nextInt(4);
        int width = gameData.getDisplayWidth();
        int height = gameData.getDisplayHeight();

        if (side == 0) {
            // Top spawn. Random location along top edge
            asteroid.setX(rnd.nextInt(width));
            asteroid.setY(0);
        } else if (side == 1) {
            // Bot spawn. Random location along bottom edge
            asteroid.setX(rnd.nextInt(width));
            asteroid.setY(height);
        } else if (side == 2) {
            // Left spawn. Random location along left edge
            asteroid.setX(0);
            asteroid.setY(rnd.nextInt(height));
        } else {
            // Right spawn. Random location along right edge
            asteroid.setX(width);
            asteroid.setY(rnd.nextInt(height));
        }

        // Make each asteroid a random bumpy rock shape
        int baseRadius = rnd.nextInt(15) + 15;
        asteroid.setRadius(baseRadius);

        int points = 7;
        double[] coordinates = new double[points * 2];

        for (int i = 0; i < points; i++) {
            // Dividing circle evenly into 7 angles
            double angle = 2 * Math.PI * i / points;

            // Adding random bumpy look to each point (-5 to +5 pixels)
            double bumpyRadius = baseRadius + (rnd.nextInt(11) - 5);

            // Converting circle math to X and Y coordinates to draw asteroid
            // X point
            coordinates[i * 2] = Math.cos(angle) * bumpyRadius;
            // Y point
            coordinates[i * 2 + 1] = Math.sin(angle) * bumpyRadius;
        }
        asteroid.setPolygonCoordinates(coordinates);
        return asteroid;
    }
}
