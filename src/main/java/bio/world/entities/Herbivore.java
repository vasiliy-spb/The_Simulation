package bio.world.entities;

import bio.world.Coordinates;

public class Herbivore extends Creature implements Hunter<Grass>{
    public Herbivore(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    void makeMove() {

    }

    @Override
    public int getDamage() {
        return 0;
    }
}
