package bio.world.entities;

public class Grass extends StaticEntity implements Prey<Herbivore> {
    private int healthPoint;

    public Grass(Coordinates coordinates) {
        super(coordinates);
        this.healthPoint = 5;
    }

    @Override
    public int getSatiety() {
        return this.healthPoint;
    }

    @Override
    public void takeDamage(Herbivore hunter) {
        healthPoint -= hunter.getDamage();
    }
}
