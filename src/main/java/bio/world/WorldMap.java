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

    public <P extends Prey<? extends Hunter<P>>, H extends Hunter<? extends Prey<H>>> Set<Coordinates> getObstaclesCoordinatesFor(Hunter<P> hunter, Prey<H> prey) {
        return null;
    }
    public Set<Coordinates> getObstaclesCoordinatesFor(Entity entity) {
//        System.out.print("For: " + entity.getCoordinates());
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
                if (creature instanceof Predator) {
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
//        System.out.println(", obstacles: " + obstacles);
        return obstacles;
    }

    public void moveCreature(Coordinates coordinates, Coordinates nextCoordinates) {
        Creature creature = creatureMap.remove(coordinates);
        creatureMap.put(nextCoordinates, creature);
    }

    public void removeStaticEntity(Grass grass) {
        staticEntityMap.remove(grass.getCoordinates(), grass);
    }
}
