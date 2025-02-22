package bio.world.entities;

public interface Prey<H extends Entity & Hunter<? extends Prey<H>>> {
    int getSatiety();

    void takeDamage(H hunter);
}
