package bio.world.entities;

public class Grass extends StaticEntity implements Prey<Herbivore> {
    private static final int INIT_HEALTH_POINT = 5;
    private int healthPoint;
    private int satiety;

    public Grass(Coordinates coordinates) {
        super(coordinates);
        this.healthPoint = INIT_HEALTH_POINT;
        this.satiety = this.healthPoint;
    }

    @Override
    public int getSatiety() {
        return satiety;
    }

    @Override
    public void takeDamage(Herbivore hunter) {
        healthPoint -= hunter.getDamage();
    }
}
