package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * This interface is used to define lifecycles for the game, ie. Player, Enemy or Asteroids.
 * The system uses this interface to load and unload entities when the game starts/stops.
*/

public interface IGamePluginService {

    /**
     * Initializes and spawns a plugin's entities into the game world.
     * <p><b>Pre-conditions:</b></p>
     * <ul>
     *     <li>{@code gameData} must be initialized and not null. It provides display boundaries.</li>
     *     <li>{@code world} must be initialized and not null. It provides the entity container.</li>
     * </ul>
     * <p><b>Post-conditions:</b></p>
     * <ul>
     *     <li>The plugin's core entities are successfully created and added to the {@code world}.</li>
     * </ul>
     *
     * @param gameData the game state and settings
     * @param world the container housing all active game entities
     */

    // Runs once when the module is loaded (Spawns entities)
    void start(GameData gameData, World world);

    /**
     * Cleans up and unloads a plugin's entities when the plugin is stopped.
     * <p><b>Pre-conditions:</b></p>
     *  <ul>
     *      <li>{@code gameData} must not be null.</li>
     *      <li>{@code world} must not be null.</li>
     *  </ul>
     *  <p><b>Post-conditions:</b></p>
     *  <ul>
     *      <li>All entities from this plugin are removed from the {@code world}.</li>
     *  </ul>
     *
     * @param gameData the game state and settings
     * @param world the container housing all active game entities
     */

    // Runs once when the module is unloaded (Cleans up entities)
    void stop(GameData gameData, World world);
}
