package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

public interface IGamePluginService {

    // Runs once when the module is loaded (Spawns entities)
    void start(GameData gameData, World world);

    // Runs once when the module is unloaded (Cleans up entities)
    void stop(GameData gameData, World world);
}
