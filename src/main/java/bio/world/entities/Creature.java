package bio.world.entities;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.List;

public abstract class Creature extends Entity {
    private int moveFrequency;
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

    protected void printEntities(List<? extends Entity> possibleTargetList) {
        for (Entity entity : possibleTargetList) {
            System.out.print(entity.getCoordinates() + ", ");
        }
        System.out.println();
    }
}
