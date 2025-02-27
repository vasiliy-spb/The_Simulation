package bio.world.entities.regular;

public interface Prey<H> {
    int getSatiety();

    void takeDamage(H hunter);
}
