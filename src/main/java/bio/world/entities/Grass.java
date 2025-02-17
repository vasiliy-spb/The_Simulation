package bio.world.entities;

import bio.world.Coordinates;

public class Grass extends StaticEntity implements Prey<Herbivore> {
    private int healthPoint;

    public Grass(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public boolean isAlive() {
        return healthPoint > 0;
    }

    @Override
    public void takeDamage(Herbivore hunter) {
        healthPoint -= hunter.getDamage();
    }
}
