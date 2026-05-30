package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * This interface defines the continuous update behavior for entities during the tick's of the game loop
 */

public interface IEntityProcessingService {

    /**
     * Processes game logic updates for the game's entities, ie. movement, key inputs and enemy behavior.
     * It is called automatically once per frame.
     * <p><b>Pre-conditions:</b></p>
     * <ul>
     *     <li>{@code gameData} must not be null. It contains active keyboard flags.</li>
     *     <li>{@code world} must not be null. It contains the entities to be affected.</li>
     * </ul>
     * <p><b>Post-conditions:</b></p>
     * <ul>
     *     <li>Entities are updated with their various logic, ie. position, rotation or states.</li>
     *     <li>New entities can be spawned, ie. bullets or enemies, into the {@code world}</li>
     * </ul>
     *
     * @param gameData the game state and settings
     * @param world the container housing all active game entities
     */

    void process(GameData gameData, World world);
}
