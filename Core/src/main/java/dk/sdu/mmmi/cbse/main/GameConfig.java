package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

// Class to tell Spring how to assemble the Game object.
// @Bean method looks unused. This is due to IntelliJ not being able to see Spring calling it at runtime.

// proxyBeanMethods = false supposedly helps with known JPMS issues
@Configuration(proxyBeanMethods = false)
class GameConfig {

    // Spring collects all beans into a list. Services are then discovered with ServiceLoader
    // + ModuleLayer in Main. Spring then dependency injects the beans into Game
    @Bean
    public Game game(List<IGamePluginService> gamePluginServices,
                     List<IEntityProcessingService> entityProcessingServices,
                     List<IPostEntityProcessingService> postEntityProcessingServices) {
        return new Game(gamePluginServices, entityProcessingServices, postEntityProcessingServices);
    }
}