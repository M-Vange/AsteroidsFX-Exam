package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Main extends Application {

    public static void main(String[] args) {

        launch(Main.class);
    }

    @Override
    public void start(Stage window) throws Exception {

        // First load services from boot layer "mods-mvn"
        // Modules like Common and CommonAsteroids are visible here
        List<IGamePluginService> plugins = ServiceLoader.load(IGamePluginService.class)
                .stream().map(ServiceLoader.Provider::get).collect(toList());

        List<IEntityProcessingService> processors = ServiceLoader.load(IEntityProcessingService.class)
                .stream().map(ServiceLoader.Provider::get).collect(toList());

        List<IPostEntityProcessingService> postProcessors = ServiceLoader.load(IPostEntityProcessingService.class)
                .stream().map(ServiceLoader.Provider::get).collect(toList());

        // Then load services from jars in plugins folder. Each jar gets a ModuleLayer to prevent split packages
        // Allows to install/uninstall of components at runtime without recompilation
        try (Stream<Path> paths = Files.list(Paths.get("plugins"))) {
            paths.filter(p -> p.toString().endsWith(".jar")).forEach(jarPath -> {

                ModuleLayer layer = createLayer(jarPath);


                // Filtering by layer. Without it, it'd add boot layer services ie. Bullet once per plugin
                // This caused bullets to move N times faster, with N being the number of plugins traversed
                ServiceLoader.load(layer, IGamePluginService.class).stream()
                        .filter(p -> p.type().getModule().getLayer() == layer)
                        .map(ServiceLoader.Provider::get)
                        .forEach(plugins::add);
                ServiceLoader.load(layer, IEntityProcessingService.class).stream()
                        .filter(p -> p.type().getModule().getLayer() == layer)
                        .map(ServiceLoader.Provider::get)
                        .forEach(processors::add);
                ServiceLoader.load(layer, IPostEntityProcessingService.class).stream()
                        .filter(p -> p.type().getModule().getLayer() == layer)
                        .map(ServiceLoader.Provider::get)
                        .forEach(postProcessors::add);
            });
        }

        // Registering each service as a Spring bean. Spring then collects and dependency injects them
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        for (int i = 0; i < plugins.size(); i++) {
            context.getBeanFactory().registerSingleton("gamePlugin_" + i, plugins.get(i));
        }
        for (int i = 0; i < processors.size(); i++) {
            context.getBeanFactory().registerSingleton("processor_" + i, processors.get(i));
        }
        for (int i = 0; i < postProcessors.size(); i++) {
            context.getBeanFactory().registerSingleton("postProcessor_" + i, postProcessors.get(i));
        }

        // Register our configuration class and start the container
        context.register(GameConfig.class);
        context.refresh();


        // Spring constructs Game and injects all service lists with @Bean method in GameConfig
        Game game = context.getBean(Game.class);

        // Starts the GUI and boots game plugins
        game.start(window);

        // Starts 60fps AnimationTimer game loop
        game.render();        

    }


    // Creating a ModuleLayer from a jar file
    // The child modules (from plugins) can see everything in the parent boot layer (mods-mvn)
    private static ModuleLayer createLayer(Path jarPath) {
        // Read module descriptor from jar
        ModuleFinder finder = ModuleFinder.of(jarPath);

        // Boot layer is parent
        ModuleLayer parent = ModuleLayer.boot();

        // Find all module names inside jar
        Set<String> modules = finder.findAll().stream().map(m -> m.descriptor().name())
                .collect(Collectors.toSet());

        // Ensure plugins can still use dependencies in parent layer
        Configuration cf = parent.configuration().resolve(finder, ModuleFinder.of(), modules);

        return parent.defineModulesWithOneLoader(cf, ClassLoader.getSystemClassLoader());
    }
}
