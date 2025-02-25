package bio.world.map;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;

import java.util.*;

public class WorldMap {
    private final int height;
    private final int width;
    private final Map<Coordinates, Entity> entities;

    public WorldMap(int height, int width) {
        this.height = height;
        this.width = width;
        this.entities = new HashMap<>();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Entity getEntityByCoordinates(Coordinates coordinates) {
        if (!entities.containsKey(coordinates)) {
            throw new IllegalArgumentException();
        }
        return entities.get(coordinates);
    }

    public List<Entity> getAllEntities() {
        return new ArrayList<>(entities.values());
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getCoordinates(), entity);
    }

    public void moveEntity(Coordinates fromCoordinates, Coordinates toCoordinates) {
        Entity entity = entities.remove(fromCoordinates);
        entities.put(toCoordinates, entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity.getCoordinates(), entity);
    }

    public Set<Coordinates> getBusyCoordinates() {
        return new HashSet<>(entities.keySet());
    }

    public boolean areBusy(Coordinates coordinates) {
        return entities.containsKey(coordinates);
    }
}
