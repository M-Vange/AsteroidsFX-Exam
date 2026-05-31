package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class EnemyPlugin implements IGamePluginService {

    private Entity enemy;

    @Override
    public void start(GameData gameData, World world) {
        // Spawns one initial enemy immediately upon boot
        enemy = createEnemy(gameData);
        world.addEntity(enemy);
    }

    // Static helper so the ControlSystem can dynamically call this to spawn more later
    public static Entity createEnemy(GameData gameData) {
        Entity enemyShip = new Enemy();

        // Enemy ship geometry
        enemyShip.setPolygonCoordinates(12,0, 10,3, 7,4, 3,4, 3,2, 1,2, 0,8, -7,8, -9,5, -9,3, -10,4, -12,3,
                -12,-3, -10,-4, -9,-3, -9,-5, -7,-8, 0,-8, 1,-2, 3,-2, 3,-4, 7,-4, 10,-3);
        enemyShip.setColor("CRIMSON");
        enemyShip.setRadius(12);

        // Picking a random border edge for spawn. 0=Top, 1=Bottom, 2=Left, 3=Right
        int edge = (int) (Math.random() * 4);

        double x = 0;
        double y = 0;
        double rotation = 0;

        double width = gameData.getDisplayWidth();
        double height = gameData.getDisplayHeight();

        // Enemies spawn inside buffer from EnemyControlSystem
        if (edge == 0) {
            // Top spawn. Spawn just outside top border, facing downward
            x = Math.random() * width;
            y = -15;
            rotation = 45 + (Math.random() * 90);
        }
        else if (edge == 1) {
            // Bot spawn. Spawn just below bottom border. Facing upwards
            x = Math.random() * width;
            y = height + 15;
            rotation = 225 + (Math.random() * 90);
        }
        else if (edge == 2) {
            // Left spawn. Spawn just outside left border. Facing to the right
            x = -15;
            y = Math.random() * height;
            rotation = -45 + (Math.random() * 90);
        }
        else {
            // Right spawn. Spawn just outside right border. Facing to the left
            x = width + 15;
            y = Math.random() * height;
            rotation = 135 + (Math.random() * 90);
        }

        // Applying the coordinates and entry angles to enemies
        enemyShip.setX(x);
        enemyShip.setY(y);
        enemyShip.setRotation(rotation);

        return enemyShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Wipe out all enemies if the module is stopped
        for (Entity e : world.getEntities(Enemy.class)) {
            world.removeEntity(e);
        }
    }
}