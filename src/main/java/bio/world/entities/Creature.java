package bio.world.entities;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.List;
import java.util.Optional;

public abstract class Creature extends Entity {
    protected int turnFrequency;
    protected int healthPoint;

    public Creature(Coordinates coordinates) {
        super(coordinates);
    }

    abstract public void makeMove(WorldMap worldMap, PathFinder pathFinder);

    protected int calculateApproximateDistance(Coordinates from, Coordinates target) {
        return Math.max(Math.abs(from.row() - target.row()), Math.abs(from.column() - target.column()));
    }

    protected boolean canEat(Entity entity) {
        int rowDiff = Math.abs(this.coordinates.row() - entity.getCoordinates().row());
        int columnDiff = Math.abs(this.coordinates.column() - entity.getCoordinates().column());
        return rowDiff < 2 && columnDiff < 2;
    }

    public boolean shouldMove(int currentTick) {
        return currentTick % turnFrequency == 0;
    }

    protected boolean hasPathFor(Entity entity, PathFinder pathFinder) {
        List<Coordinates> path = pathFinder.find(this.coordinates, entity.getCoordinates());
        return !path.isEmpty() && path.get(path.size() - 1).equals(entity.getCoordinates());
    }

    protected void makeRandomStep(WorldMap worldMap, PathFinder pathFinder) {
        Optional<Coordinates> nextCoordinatesContainer = pathFinder.findRandomStepFrom(this.coordinates);
        if (nextCoordinatesContainer.isEmpty()) {
            return;
        }
        Coordinates nextCoordinates = nextCoordinatesContainer.get();
        worldMap.moveEntity(this.coordinates, nextCoordinates);
        this.setCoordinates(nextCoordinates);
    }

    protected void printEntities(List<? extends Entity> possibleTargetList) {
        for (Entity entity : possibleTargetList) {
            System.out.print(entity.getCoordinates() + ", ");
        }
        System.out.println();
    }
}
