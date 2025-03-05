package bio.world.entities.regular;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.entities.statical.trap.Trap;
import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public abstract class Human extends Entity {
    private static final Set<Class<? extends Entity>> NOT_OBSTACLES_TYPES_FOR_MOVE = Set.of(Grass.class);
    private static final Predicate<Entity> NOT_OBSTACLES_FOR_MOVE_CHECKER = e -> {
        if (e instanceof Trap trap) {
            return !trap.hasCapturedCreature();
        }
        return NOT_OBSTACLES_TYPES_FOR_MOVE.contains(e.getClass());
    };
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
        Optional<Coordinates> nextCoordinatesContainer = pathFinder.findRandomStepFrom(this.coordinates, NOT_OBSTACLES_FOR_MOVE_CHECKER);

        if (nextCoordinatesContainer.isEmpty()) {
            return;
        }

        Coordinates nextCoordinates = nextCoordinatesContainer.get();
        moveTo(nextCoordinates, worldMap);
    }

    private void moveTo(Coordinates nextCoordinates, WorldMap worldMap) {
        worldMap.removeEntity(this);
        this.setCoordinates(nextCoordinates);
        worldMap.addEntity(this);
    }
}
