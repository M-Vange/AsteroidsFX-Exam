package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
public class PlayerPlugin implements IGamePluginService {

    private Entity player;

    public PlayerPlugin() {
    }

    // Runs once when the game boots. Creates player ship and places into world entity list
    @Override
    public void start(GameData gameData, World world) {

        // Add entities to the world
        player = createPlayerShip(gameData);
        world.addEntity(player);
    }

    // Constructs the player ship object
    private Entity createPlayerShip(GameData gameData) {

        // Defining the geometry of the player ship
        Entity playerShip = new Player();

        // New cooler complex player geometry
        // Array holder for player ship geometry
        double[] myCoordinates = new double[]{
                10,0, 9,1, 7,2, 3,2, 0,6, -5,6, -3,5, -2,3, -2,2, -5,2, -5,1, -7,2,
                -7,-2, -5,-1, -5,-2, -2,-2, -2,-3, -3,-5, -5,-6, 0,-6, 3,-2, 7,-2, 9,-1
        };

        // Setting a scaling factor and applying to geometry
        double scaleFactor = 1.8;
        for (int i=0; i < myCoordinates.length; i++) {
            myCoordinates[i] *= scaleFactor;
        }

        // Is handed the upscaled coordinates
        playerShip.setPolygonCoordinates(myCoordinates);

        playerShip.setX(gameData.getDisplayHeight()/2);
        playerShip.setY(gameData.getDisplayWidth()/2);

        // Increases radius of player ship according to scale
        // Adjust between a radius of 9 or 10 depending on feel
        playerShip.setRadius((float)(9 * scaleFactor));

        // Assigning player ship a color
        playerShip.setColor("CORNFLOWERBLUE");

        return playerShip;
    }

    // Removes player ship from the world if module is disabled / closed
    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        world.removeEntity(player);
    }

}
