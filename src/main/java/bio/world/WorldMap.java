package bio.world;

import bio.world.entities.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public boolean areBusy(Coordinates coordinates) {
        return entities.containsKey(coordinates);
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getCoordinates(), entity);
    }

    public Set<Coordinates> getBusyCoordinates() {
        Set<Coordinates> busyCoordinates = new HashSet<>(entities.keySet());
        return busyCoordinates;
    }

    public Entity getEntityByCoordinates(Coordinates coordinates) {
        if (!entities.containsKey(coordinates)) {
            throw new IllegalArgumentException();
        }
        return entities.get(coordinates);
    }

    public Set<Creature> getCreatures() {
        Set<Creature> creatures = entities.values()
                .stream()
                .filter(e -> e instanceof Creature)
                .map(e -> (Creature) e)
                .collect(Collectors.toSet());
        return creatures;
    }

    public Set<StaticEntity> getStaticEntities() {
        Set<StaticEntity> staticEntitySet = entities.values()
                .stream()
                .filter(e -> e instanceof StaticEntity)
                .map(e -> (StaticEntity) e)
                .collect(Collectors.toSet());
        return staticEntitySet;
    }

    public Set<Coordinates> getObstaclesCoordinatesFor(Entity entity) {
        Set<Coordinates> obstacles = new HashSet<>();
        if (entity instanceof Herbivore) {
            for (Entity e : entities.values()) {
                if (e instanceof Grass) {
                    continue;
                }
                obstacles.add(e.getCoordinates());
            }
        } else if (entity instanceof Predator) {
            for (Entity e : entities.values()) {
                if (e instanceof Grass || e instanceof Herbivore) {
                    continue;
                }
                obstacles.add(e.getCoordinates());
            }
        }
        return obstacles;
    }

    public void moveEntity(Coordinates fromCoordinates, Coordinates toCoordinates) {
        Entity entity = entities.remove(fromCoordinates);
        entities.put(toCoordinates, entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity.getCoordinates(), entity);
    }
}
