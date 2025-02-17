package bio.world.entities;

import bio.world.Coordinates;

public abstract class Creature extends Entity{
    private int moveFrequency;
    private int healthPoint;

    public Creature(Coordinates coordinates) {
        super(coordinates);
    }

    abstract void makeMove();
}
