package bio.world.entities;

import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.*;

public abstract class Creature extends Entity {
    protected int healthPoint;
    protected int turnFrequency;
    protected final int attackPower;
    protected int countMoveWithoutFood;
    protected final Comparator<Entity> priorityTargetComparator;

    public Creature(Coordinates coordinates, int healthPoint, int turnFrequency, int attackPower, int countMoveWithoutFood) {
        super(coordinates);
        this.healthPoint = healthPoint;
        this.turnFrequency = turnFrequency;
        this.attackPower = attackPower;
        this.countMoveWithoutFood = countMoveWithoutFood;
        this.priorityTargetComparator = (t1, t2) ->
                calculateApproximateDistance(this.coordinates, t1.getCoordinates()) -
                        calculateApproximateDistance(this.coordinates, t2.getCoordinates());
    }

    abstract public void makeMove(WorldMap worldMap, PathFinder pathFinder);

    protected boolean isAlive() {
        return this.healthPoint > 0;
    }

    protected abstract List<? extends Entity> getTargetEntities(WorldMap worldMap);

    protected Set<Coordinates> getObstaclesCoordinates(WorldMap worldMap, Set<Class<? extends Entity>> notObstaclesTypes) {
        List<Entity> entities = worldMap.getAllEntities();
        Set<Coordinates> obstacles = new HashSet<>();
        for (Entity entity : entities) {
            if (notObstaclesTypes.contains(entity.getClass())) {
                continue;
            }
            obstacles.add(entity.getCoordinates());
        }
        return obstacles;
    }

    protected void makeRandomStep(WorldMap worldMap, PathFinder pathFinder) {
        Optional<Coordinates> nextCoordinatesContainer = pathFinder.findRandomStepFrom(this.coordinates);
        if (nextCoordinatesContainer.isEmpty()) {
            return;
        }
        Coordinates nextCoordinates = nextCoordinatesContainer.get();
        moveTo(nextCoordinates, worldMap);
    }

    protected boolean canAttack(Entity entity) {
        int rowDiff = Math.abs(this.coordinates.row() - entity.getCoordinates().row());
        int columnDiff = Math.abs(this.coordinates.column() - entity.getCoordinates().column());
        return rowDiff < 2 && columnDiff < 2;
    }

    protected void moveTo(Coordinates nextCoordinates, WorldMap worldMap) {
        worldMap.moveEntity(this.coordinates, nextCoordinates);
        this.setCoordinates(nextCoordinates);
    }

    public boolean shouldMove(int currentTick) {
        return currentTick % turnFrequency == 0;
    }

    private int calculateApproximateDistance(Coordinates from, Coordinates target) {
        return Math.max(Math.abs(from.row() - target.row()), Math.abs(from.column() - target.column()));
    }
}
