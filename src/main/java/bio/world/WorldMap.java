package bio.world;

import bio.world.entities.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorldMap {
    private final int height;
    private final int width;
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

    public void addCreature(Creature creature) {
        creatureMap.put(creature.getCoordinates(), creature);
    }

    public Set<Creature> getCreatures() {
        Set<Creature> creatures = new HashSet<>(creatureMap.values());
        return creatures;
    }

    public Set<StaticEntity> getStaticEntities() {
        Set<StaticEntity> staticEntitySet = new HashSet<>(staticEntityMap.values());
        return staticEntitySet;
    }

    public Set<Coordinates> getObstaclesCoordinatesFor(Entity entity) {
        Set<Coordinates> obstacles = new HashSet<>();
        if (entity instanceof Herbivore) {
            obstacles.addAll(creatureMap.keySet());
            for (StaticEntity staticEntity : staticEntityMap.values()) {
                if (staticEntity instanceof Grass) {
                    continue;
                }
                obstacles.add(staticEntity.getCoordinates());
            }
        } else if (entity instanceof Predator) {
            for (Creature creature : creatureMap.values()) {
                if (creature instanceof Herbivore) {
                    continue;
                }
                obstacles.add(creature.getCoordinates());
            }
            for (StaticEntity staticEntity : staticEntityMap.values()) {
                if (staticEntity instanceof Grass) {
                    continue;
                }
                obstacles.add(staticEntity.getCoordinates());
            }
        }
        return obstacles;
    }

    public void moveCreature(Coordinates fromCoordinates, Coordinates toCoordinates) {
        Creature creature = creatureMap.remove(fromCoordinates);
        creatureMap.put(toCoordinates, creature);
        staticEntityMap.remove(toCoordinates);
    }

    public void removeStaticEntity(StaticEntity staticEntity) {
        staticEntityMap.remove(staticEntity.getCoordinates(), staticEntity);
    }

    public void removeCreature(Creature creature) {
        creatureMap.remove(creature.getCoordinates(), creature);
    }
}
