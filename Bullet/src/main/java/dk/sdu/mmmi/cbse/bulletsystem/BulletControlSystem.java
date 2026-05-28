package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

// Implementing BulletSPI
public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    @Override
    public void process(GameData gameData, World world) {

        // Code affecting bullet movement / behavior
        for (Entity bullet : world.getEntities(Bullet.class)) {
            double changeX = Math.cos(Math.toRadians(bullet.getRotation()));
            double changeY = Math.sin(Math.toRadians(bullet.getRotation()));
            // Alter numeric values here for bullet speed
            bullet.setX(bullet.getX() + changeX * 3);
            bullet.setY(bullet.getY() + changeY * 3);

            // Removing bullets outside of screen bounds
            int boundPadding = 10;
            if (bullet.getX() < 0 || bullet.getX() > gameData.getDisplayWidth() + boundPadding ||
            bullet.getY() <0 || bullet.getY() > gameData.getDisplayHeight() + boundPadding) {
                world.removeEntity(bullet);
            }
        }
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity bullet = new Bullet();
        // Altered bullet geometry
        bullet.setPolygonCoordinates(1.5,0, 1,0.5, 1,1, 0.5,1, 0,1.5, -0.5,1, -1,1, -1,0.5, -1.5,0,
                -1,-0.5, -1,-1, -0.5,-1, 0,-1.5, 0.5,-1, 1,-1, 1,-0.5);

        double changeX = Math.cos(Math.toRadians(shooter.getRotation()));
        double changeY = Math.sin(Math.toRadians(shooter.getRotation()));

        // Implemented due to suicide bullets spawning inside player ship radius
        // Dynamically spawn bullets outside of the shooter's radius
        double spawnDistance = shooter.getRadius() + 2;

        // Replaced hardcoded value with spawnDistance
        bullet.setX(shooter.getX() + changeX * spawnDistance);
        bullet.setY(shooter.getY() + changeY * spawnDistance);

        bullet.setRotation(shooter.getRotation());
        bullet.setRadius(1.5F);
        return bullet;
    }
}
