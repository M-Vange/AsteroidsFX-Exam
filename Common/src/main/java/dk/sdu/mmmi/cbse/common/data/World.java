package dk.sdu.mmmi.cbse.common.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    // Thread safe way to hold all the entities in the game
    private final Map<String, Entity> entityMap = new ConcurrentHashMap<>();

    public String addEntity(Entity entity) {
        entityMap.put(entity.getID(), entity);
        return entity.getID();
    }

    public void removeEntity(String entityID) {
        entityMap.remove(entityID);
    }

    public void removeEntity(Entity entity) {
        entityMap.remove(entity.getID());
    }

    // Returns a list of every entity in the game
    public Collection<Entity> getEntities() {
        return entityMap.values();
    }

    // A filter. Lets me ask it for specific things, like getEntities(Asteroid.class)
    public final <E extends Entity> List<Entity> getEntities(Class<E>... entityTypes) {
        List<Entity> filteredList = new ArrayList<>();

        // Goes through every entity in the game
        for (Entity e : getEntities()) {
            // Looking for specific needed entity
            for (Class<E> entityType : entityTypes) {
                if (entityType.equals(e.getClass())) {
                    filteredList.add(e);
                }
            }
        }
        return filteredList;
    }

    public Entity getEntity(String ID) {
        return entityMap.get(ID);
    }
}
