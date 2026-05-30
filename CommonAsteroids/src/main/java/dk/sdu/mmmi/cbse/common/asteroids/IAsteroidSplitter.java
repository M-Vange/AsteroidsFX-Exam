package dk.sdu.mmmi.cbse.common.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * This interface provides a contract for splitting a parent asteroid into smaller pieces upon being hit by a bullet.
 */

public interface IAsteroidSplitter {

    /**
     * Spawns smaller asteroid entities in the game world if the parent asteroid meets size requirements.
     * <p><b>Pre-conditions:</b></p>
     * <ul>
     *     <li>{@code e} must not be null. Must be a valid asteroid entity and must have triggered a collision with a bullet entity.</li>
     *     <li>{@code w} must not be null. Must represent the active game world.</li>
     * </ul>
     * <p><b>Post-conditions:</b></p>
     * <ul>
     *     <li>The asteroid parent entity {@code e} is evaluated and asteroid pieces are instantiated.</li>
     *     <li>The new asteroid pieces are injected into the active game {@code World w}.</li>
     * </ul>
     *
     * @param e the parent asteroid entity being split
     * @param w the game world where asteroid pieces will be spawned
     */

    void createSplitAsteroid(Entity e, World w);
}
