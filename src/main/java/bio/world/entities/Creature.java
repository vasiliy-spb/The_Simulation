package bio.world.entities;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.path_finders.PathFinder;

public abstract class Creature extends Entity{
    private int moveFrequency;
    protected int healthPoint;

    public Creature(Coordinates coordinates) {
        super(coordinates);
    }

    abstract public void makeMove(WorldMap worldMap, PathFinder pathFinder);
}
