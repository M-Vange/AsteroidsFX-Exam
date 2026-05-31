package dk.sdu.mmmi.cbse.main;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main extends Application {

    // A List holding child layers for split package resolution
    public static final List<ModuleLayer> pluginLayers = new ArrayList<>();

    // Helper method building isolated layers for modules
    private static ModuleLayer createLayer(File jarFile, String moduleName) {
        // Looking inside specified jar file
        ModuleFinder finder = ModuleFinder.of(jarFile.toPath());

        // Using default boot layer as a parent
        ModuleLayer parent = ModuleLayer.boot();

        // Resolve dependencies so jar can communicate with Core and Common
        Configuration cf = parent.configuration().resolveAndBind(finder, ModuleFinder.of(), Set.of(moduleName));

        // Instantiating layer with its own class loader
        return parent.defineModulesWithOneLoader(cf, ClassLoader.getSystemClassLoader());
    }

    public static void main(String[] args) {
        // Looking inside plugins directory
        File dir = Paths.get("plugins").toFile();

        // Ensuring folder exists and contains files
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".jar"));

            if (files != null) {
                for (File file : files) {
                    // Scan the jar file
                    ModuleFinder finder = ModuleFinder.of(file.toPath());

                    // Find first module name given inside jar file
                    finder.findAll().stream().findFirst().ifPresent(module -> {
                        String moduleName = module.descriptor().name();

                        // Send to helper method to build own isolated layer
                        ModuleLayer layer = createLayer(file, moduleName);

                        // Save layer to list so game loop can access it
                        pluginLayers.add(layer);
                    });
                }
            }
        }

        // Start the JavaFX application context
        launch(Main.class);
    }

    @Override
    public void start(Stage window) throws Exception {

        // Game constructor queries ServiceLocator to locate plugins inside "mods-mvn"
        Game game = new Game();

        // Starts the GUI and boots game plugins
        game.start(window);

        // Starts 60fps AnimationTimer game loop
        game.render();        

    }

}
