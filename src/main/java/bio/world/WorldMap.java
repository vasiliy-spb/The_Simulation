package bio.world;

import bio.world.entities.Creature;
import bio.world.entities.Entity;
import bio.world.entities.StaticEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorldMap {
    private int height;
    private int width;
    private final Map<Coordinates, Creature> creatureMap;
    private final Map<Coordinates, StaticEntity> staticEntityMap;

    public WorldMap(int height, int width) {
        this.height = height;
        this.width = width;
        this.creatureMap = new HashMap<>();
        staticEntityMap = new HashMap<>();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean areBusy(Coordinates coordinates) {
        return creatureMap.containsKey(coordinates) || staticEntityMap.containsKey(coordinates);
    }

    public void addStaticEntity(StaticEntity staticEntity) {
        staticEntityMap.put(staticEntity.getCoordinates(), staticEntity);
    }

    public Set<Coordinates> getBusyCoordinates() {
        Set<Coordinates> busyCoordinates = new HashSet<>();
        busyCoordinates.addAll(creatureMap.keySet());
        busyCoordinates.addAll(staticEntityMap.keySet());
        return busyCoordinates;
    }

    public Entity getEntityByCoordinates(Coordinates coordinates) {
        if (creatureMap.containsKey(coordinates)) {
            return creatureMap.get(coordinates);
        }
        if (staticEntityMap.containsKey(coordinates)) {
            return staticEntityMap.get(coordinates);
        }
        throw new IllegalArgumentException();
    }
}
