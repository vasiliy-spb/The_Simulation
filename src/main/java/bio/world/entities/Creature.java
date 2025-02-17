package bio.world.entities;

public abstract class Creature extends Entity{
    private int speed;
    private int healthPoint;
    abstract void makeMove();
}
