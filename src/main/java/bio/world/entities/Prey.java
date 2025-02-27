package bio.world.entities;

public interface Prey<H> {
    int getSatiety();

    void takeDamage(H hunter);
}
