package dk.sdu.mmmi.cbse.common.util;

import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

// Singleton enum - Ensures only one plugin detector is running

public enum ServiceLocator {

    INSTANCE;

    private static final Map<Class, ServiceLoader> loadermap = new HashMap<>();
    private final ModuleLayer layer;

    // The ServiceLocator allows drag and drop of modules
    // Rather than manually adding modules to the core, this class locates them

    ServiceLocator() {
        try {
            // Looks for the "plugins" directory
            Path pluginsDir = Paths.get("plugins");

            // Scans "plugins" for Java modules (.jar files)
            ModuleFinder pluginsFinder = ModuleFinder.of(pluginsDir);

            // Find all names of all found plugin modules
            List<String> plugins = pluginsFinder
                    .findAll()
                    .stream()
                    .map(ModuleReference::descriptor)
                    .map(ModuleDescriptor::name)
                    .collect(Collectors.toList());

            // Checks the code inside "plugins" directory
            // Verifies the plugins aren't missing dependencies
            Configuration pluginsConfiguration = ModuleLayer
                    .boot()
                    .configuration()
                    .resolve(pluginsFinder, ModuleFinder.of(), plugins);

            // Create a module layer for plugins
            // Tells Java to load all the plugins found into the memory and lets the game interact with them
            layer = ModuleLayer
                    .boot()
                    .defineModulesWithOneLoader(pluginsConfiguration, ClassLoader.getSystemClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    // Method acts like a search filter
    public <T> List<T> locateAll(Class<T> service) {
        ServiceLoader<T> loader = loadermap.get(service);

        if (loader == null) {
            loader = ServiceLoader.load(layer, service);
            loadermap.put(service, loader);
        }

        List<T> list = new ArrayList<T>();

        if (loader != null) {
            try {
                for (T instance : loader) {
                    list.add(instance);
                }
            } catch (ServiceConfigurationError serviceError) {
                serviceError.printStackTrace();
            }
        }

        return list;
    }

}
