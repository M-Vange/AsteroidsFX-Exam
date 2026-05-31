package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;
import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {

    // Staggering enemy spawn rate (6 seconds with 360 frames at 60 fps)
    private int spawnCooldown = 360;

    @Override
    public void process(GameData gameData, World world) {
        Collection<Entity> enemies = world.getEntities(Enemy.class);

        // Spawning up to 3 enemies with 10 seconds between spawns
        if (enemies.size() < 3) {
            spawnCooldown--;
            if (spawnCooldown <= 0) {
                world.addEntity(EnemyPlugin.createEnemy(gameData));
                spawnCooldown = 600;
            }
        }

        // Enemy processing
        for (Entity entity : enemies) {
            Enemy enemy = (Enemy) entity;

            // Decision maker updating every few split seconds
            if (enemy.getDecisionTimer() <= 0) {
                // Randomly pick a state 0=Idle, 1=Forward, 2=Turn Left, 3=Turn Right
                enemy.setMovementState((int) (Math.random() * 4));

                // Commit to this decision for a random window between 20 and 70 frames
                enemy.setDecisionTimer(20 + (int) (Math.random() * 50));

                // Random shooting logic with a 25% chance to trigger when a state is changed
                if (Math.random() < 0.25) {
                    getBulletSPIs().stream().findFirst().ifPresent(spi -> {
                        world.addEntity(spi.createBullet(enemy, gameData));
                    });
                }
            } else {
                enemy.setDecisionTimer(enemy.getDecisionTimer() - 1);
            }

            // Handling enemy movement
            // Move forward
            if (enemy.getMovementState() == 1) {
                double changeX = Math.cos(Math.toRadians(enemy.getRotation()));
                double changeY = Math.sin(Math.toRadians(enemy.getRotation()));
                // Moving slightly slower than the player (1.2 speed) for balance
                enemy.setX(enemy.getX() + changeX * 1.2);
                enemy.setY(enemy.getY() + changeY * 1.2);
                // Turn Left
            } else if (enemy.getMovementState() == 2) {
                enemy.setRotation(enemy.getRotation() - 3.5);
                // Turn Right
            } else if (enemy.getMovementState() == 3) {
                enemy.setRotation(enemy.getRotation() + 3.5);
            }

            // Screen wrapping. Makes enemies able to stay on screen rather than moving outside bounds
            if (enemy.getX() < 0) enemy.setX(gameData.getDisplayWidth());
            if (enemy.getX() > gameData.getDisplayWidth()) enemy.setX(0);
            if (enemy.getY() < 0) enemy.setY(gameData.getDisplayHeight());
            if (enemy.getY() > gameData.getDisplayHeight()) enemy.setY(0);

            // Screen wrapping. Making enemies not able to fly continuously outside of bounds
            // Buffer for boundary, hidden outside borders
            double buffer = 30.0;
            double screenWidth = gameData.getDisplayWidth();
            double screenHeight = gameData.getDisplayHeight();

            // If an enemy flies too far left past buffer, warp them to the right side
            if (enemy.getX() < -buffer) {
                enemy.setX(screenWidth + buffer - 5);
            }
            // If an enemy flies too far right past buffer, warp them to the left side
            if (enemy.getX() > screenWidth + buffer) {
                enemy.setX(-buffer + 5);
            }
            // If an enemy flies too far up past buffer, warp them to the bottom
            if (enemy.getY() < -buffer) {
                enemy.setY(screenHeight + buffer - 5);
            }
            // If an enemy flies too far down past buffer, warp them to the top
            if (enemy.getY() > screenHeight + buffer) {
                enemy.setY(-buffer + 5);
            }
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}