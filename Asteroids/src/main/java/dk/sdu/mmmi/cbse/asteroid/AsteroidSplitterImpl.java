package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import java.util.Random;


public class AsteroidSplitterImpl implements IAsteroidSplitter {

    private final Random rnd = new Random();

    @Override
    public void createSplitAsteroid(Entity e, World world) {
        Asteroid original = (Asteroid) e;

        // Getting current size of hit asteroid
        float originalRadius = original.getRadius();

        // Decision maker determining if it should split or be destroyed
        // If radius >= 12 pixels, it splits
        if (originalRadius >= 12) {
            float newRadius = originalRadius / 2;

            // Spawning two smaller asteroid pieces
            for (int i = 0; i < 2; i++) {
                // Assigning split pieces new flying values
                double randomAngle = rnd.nextInt(360);
                // Smaller pieces get assigned a faster speed than original asteroids
                double fasterSpeed = original.getSpeed() * 0.8;
                double randomSpin = 1.0 + rnd.nextDouble() * 2.0;

                Asteroid smallAsteroid = new Asteroid(fasterSpeed,  randomAngle, randomSpin);

                // Position new split pieces where original asteroid was destroyed
                smallAsteroid.setX(original.getX());
                smallAsteroid.setY(original.getY());
                smallAsteroid.setRadius(newRadius);

                // Generating random bumpy shape for split pieces
                int points = 7;
                double[] coordinates = new double[points * 2];
                for (int j = 0; j < points; j++) {
                    double angle = 2 * Math.PI * j / points;
                    double bumpyRadius = newRadius + (rnd.nextInt(5) - 2);
                    coordinates[j * 2] = Math.cos(angle) * bumpyRadius;
                    coordinates[j * 2 + 1] = Math.sin(angle) * bumpyRadius;
                }
                smallAsteroid.setPolygonCoordinates(coordinates);

                // Spawning split asteroid pieces in game world
                world.addEntity(smallAsteroid);
            }
        }

        // Removing original asteroid from game world
        world.removeEntity(original);

    }

}
