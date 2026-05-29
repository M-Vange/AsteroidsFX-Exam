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

    // Adding <?> lets java know it should accept any type of class/serviceloader using wildcards aka <?>
    private static final Map<Class<?>, ServiceLoader<?>> loadermap = new HashMap<>();

    // The ServiceLocator allows drag and drop of modules
    // Rather than manually adding modules to the core, this class locates them

    ServiceLocator() {
        // Maven loads all modules from "mods-mvn" automatically on startup
        // No need to scan folders
    }


    // Method acts like a search filter
    public <T> List<T> locateAll(Class<T> service) {
        @SuppressWarnings("unchecked")
        ServiceLoader<T> loader = (ServiceLoader<T>) loadermap.get(service);

        if (loader == null) {
            loader = ServiceLoader.load(ModuleLayer.boot(), service);
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
