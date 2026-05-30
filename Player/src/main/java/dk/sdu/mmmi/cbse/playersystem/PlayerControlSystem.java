package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;


public class PlayerControlSystem implements IEntityProcessingService {

    // Adding field to hold visual thruster
    // Not creating new module due to dependency issues
    private Entity thruster = null;

    // Gun cooldown. Tracks frame between designated shots
    private int shootCooldown = 0;

    // Small custom entity class for showing player health in window
    private static class HealthIcon extends Entity {}

    @Override
    public void process(GameData gameData, World world) {
            
        for (Entity player : world.getEntities(Player.class)) {

            // Decrease gun cooldown every frame
            if (shootCooldown > 0) {
                shootCooldown--;
            }

            // Reduced rotation movement speed by half for smoother controls
            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                player.setRotation(player.getRotation() - 2.5);
            }
            if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                player.setRotation(player.getRotation() + 2.5);
            }
            if (gameData.getKeys().isDown(GameKeys.UP)) {
                double changeX = Math.cos(Math.toRadians(player.getRotation()));
                double changeY = Math.sin(Math.toRadians(player.getRotation()));
                player.setX(player.getX() + changeX);
                player.setY(player.getY() + changeY);
            }
            if (gameData.getKeys().isDown(GameKeys.BACK)){
                double changeX = Math.cos(Math.toRadians(player.getRotation()));
                double changeY = Math.sin(Math.toRadians(player.getRotation()));
                // Subtract instead of add to move ship in reverse
                // Halved the backwards movement speed
                player.setX(player.getX() - changeX * 0.5);
                player.setY(player.getY() - changeY * 0.5);
            }
            // Added shootCooldown condition to counteract bullet spam
            if(gameData.getKeys().isDown(GameKeys.SPACE) && shootCooldown == 0) {
                getBulletSPIs().stream().findFirst().ifPresent(
                        spi -> {world.addEntity(spi.createBullet(player, gameData));}
                );

                // Set timer for shots. 15 frames = 0.25 seconds between shots
                shootCooldown = 15;

                // Adding recoil when firing
                double changeX = Math.cos(Math.toRadians(player.getRotation()));
                double changeY = Math.sin(Math.toRadians(player.getRotation()));
                player.setX(player.getX() - changeX * 0.15);
                player.setY(player.getY() - changeY * 0.15);
            }

            // Player health ui logic
            // Remove lost health icons
            for (Entity oldIcon : world.getEntities(HealthIcon.class)) {
                world.removeEntity(oldIcon);
            }

            // Rendering green diamond player health icons
            int currentHealth = player.getHealth();
            for (int i = 0; i < currentHealth; i++) {
                Entity heart = new HealthIcon();
                // Drawing diamond health geometry
                heart.setPolygonCoordinates(0,9, 7,0, 0,-9, -7,0);
                heart.setColor("MEDIUMSPRINGGREEN");
                // Setting radius to 0 so entities don't crash into the UI
                heart.setRadius(0);

                // Place health ui in top left corner of window
                heart.setX(25 + (i * 22));
                heart.setY(55);

                world.addEntity(heart);
            }
            
        if (player.getX() < 0) {
            player.setX(1);
        }

        if (player.getX() > gameData.getDisplayWidth()) {
            player.setX(gameData.getDisplayWidth()-1);
        }

        if (player.getY() < 0) {
            player.setY(1);
        }

        if (player.getY() > gameData.getDisplayHeight()) {
            player.setY(gameData.getDisplayHeight()-1);
        }

            // Adding thruster when player moves forward
            if (gameData.getKeys().isDown(GameKeys.UP)) {

                // Creating thruster
                if (thruster == null) {
                    thruster = new Entity() {};

                    thruster.setPolygonCoordinates(6, -3, -6, 0, 6, 3);
                    thruster.setColor("#FF4500"); // Orange-Red

                    // Removing thruster radius to avoid collision
                    thruster.setRadius(0);

                    world.addEntity(thruster);
                }

                double changeX = Math.cos(Math.toRadians(player.getRotation()));
                double changeY = Math.sin(Math.toRadians(player.getRotation()));

                // Moving thruster's center point outside player radius
                double distanceBehindPlayer = player.getRadius() + 7.5;

                thruster.setX(player.getX() - changeX * distanceBehindPlayer);
                thruster.setY(player.getY() - changeY * distanceBehindPlayer);
                thruster.setRotation(player.getRotation());

            } else {
                // Removes thruster when player stops pressing 'W'
                if (thruster != null) {
                    world.removeEntity(thruster);
                    thruster = null;
                }
            }
                                        
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
