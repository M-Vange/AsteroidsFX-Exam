package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * This interface defines what happens at the end of a game loop tick.
 * This is primarily used for detecting collisions, screen-wrapping and cleanup.
 */

public interface IPostEntityProcessingService {

    /**
     * Handles the final checks for processing of entities in the game world.
     * <p><b>Pre-conditions:</b></p>
     * <ul>
     *     <li>{@code gameData} must not be null.</li>
     *     <li>{@code world} must not be null. And it must contain any entities that have finished an update.</li>
     * </ul>
     * <p><b>Post-conditions:</b></p>
     * <ul>
     *     <li>Interactions for entities have been processed, ie. a collision.</li>
     *     <li>Entities that have been flagged as destroyed by any interaction are removed from the {@code world}.</li>
     * </ul>
     *
     * @param gameData the game state and settings
     * @param world the container housing all active game entities
     */

    void process(GameData gameData, World world);
}
