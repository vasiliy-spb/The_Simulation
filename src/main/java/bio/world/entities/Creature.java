package bio.world.entities;

import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public abstract class Creature extends Entity {
    protected int healthPoint;
    protected int turnFrequency;
    protected final int attackPower;
    private final Comparator<Entity> priorityTargetComparator;

    public Creature(Coordinates coordinates, int attackPower) {
        super(coordinates);
        this.attackPower = attackPower;
        this.priorityTargetComparator = (t1, t2) ->
                calculateApproximateDistance(this.coordinates, t1.getCoordinates()) -
                        calculateApproximateDistance(this.coordinates, t2.getCoordinates());
    }

    abstract public void makeMove(WorldMap worldMap, PathFinder pathFinder);

    protected boolean isAlive() {
        return this.healthPoint > 0;
    }

    protected Optional<Entity> findNearestTarget(WorldMap worldMap, PathFinder pathFinder) {
        List<Entity> targets = worldMap.getTargetsFor(this.getClass());
        targets = targets.stream()
                .sorted(this.priorityTargetComparator)
                .toList();
        for (Entity target : targets) {
            if (hasPathFor(target, pathFinder)) {
                return Optional.of(target);
            }
        }
        return Optional.empty();
    }

    private boolean hasPathFor(Entity entity, PathFinder pathFinder) {
        List<Coordinates> path = pathFinder.find(this.coordinates, entity.getCoordinates());
        return !path.isEmpty() && path.get(path.size() - 1).equals(entity.getCoordinates());
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
