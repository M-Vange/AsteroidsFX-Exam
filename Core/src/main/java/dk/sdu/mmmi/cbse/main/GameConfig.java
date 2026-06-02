package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

// Class to tell Spring how to assemble the Game object.
// @Bean method looks unused. This is due to IntelliJ not being able to see Spring calling it at runtime.

// proxyBeanMethods = false supposedly helps with known JPMS issues
@Configuration(proxyBeanMethods = false)
class GameConfig {

    // RestTemplate is Spring's built-in HTTP client.
    // Defining it as a bean means Spring manages it and can inject it where needed.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // ScoreClient wraps the RestTemplate and handles score-related HTTP calls.
    // Spring injects the RestTemplate bean above into this automatically.
    @Bean
    public ScoreClient scoreClient(RestTemplate restTemplate) {
        return new ScoreClient(restTemplate);
    }

    // Spring collects all beans into a list. Services are then discovered with ServiceLoader
    // + ModuleLayer in Main. Spring then dependency injects the beans into Game
    @Bean
    public Game game(List<IGamePluginService> gamePluginServices,
                     List<IEntityProcessingService> entityProcessingServices,
                     List<IPostEntityProcessingService> postEntityProcessingServices,
                     ScoreClient scoreClient) {
        return new Game(gamePluginServices, entityProcessingServices, postEntityProcessingServices, scoreClient);
    }
}