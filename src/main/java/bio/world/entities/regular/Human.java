package bio.world.entities.regular;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.Optional;
import java.util.Set;

public abstract class Human extends Entity {
    private final Set<Class<? extends Entity>> NOT_OBSTACLES_TYPES_FOR_MOVE = Set.of(Grass.class);
    protected int healthPoint;
    protected int turnFrequency;

    public Human(Coordinates coordinates, int healthPoint, int turnFrequency) {
        super(coordinates);
        this.healthPoint = healthPoint;
        this.turnFrequency = turnFrequency;
    }

    abstract public void makeMove(WorldMap worldMap, PathFinder pathFinder);

    public boolean shouldMove(int currentTick) {
        return currentTick % turnFrequency == 0;
    }

    protected void makeRandomStep(WorldMap worldMap, PathFinder pathFinder) {
        Optional<Coordinates> nextCoordinatesContainer = pathFinder.findRandomStepFrom(this.coordinates, NOT_OBSTACLES_TYPES_FOR_MOVE);

        if (nextCoordinatesContainer.isEmpty()) {
            return;
        }

        Coordinates nextCoordinates = nextCoordinatesContainer.get();
        moveTo(nextCoordinates, worldMap);
    }

    private void moveTo(Coordinates nextCoordinates, WorldMap worldMap) {
//        worldMap.moveEntity(this.coordinates, nextCoordinates);
        worldMap.removeEntity(this);
        this.setCoordinates(nextCoordinates);
        worldMap.addEntity(this);
    }
}
