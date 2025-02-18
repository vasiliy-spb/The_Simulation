package bio.world.entities;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.path_finders.PathFinder;

public class Predator extends Creature {
    private int attackPower;

    public Predator(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {

    }
}
