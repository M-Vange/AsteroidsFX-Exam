package dk.sdu.mmmi.cbse.common.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

// SPI = Service provider interface
// Contract - any class wanting to use bullets must have a method called createBullet
public interface BulletSPI {
    Entity createBullet(Entity e, GameData gameData);
}
