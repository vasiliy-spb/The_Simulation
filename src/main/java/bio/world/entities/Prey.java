package bio.world.entities;

public interface Prey<H extends Hunter<? extends Prey<H>>> {
    boolean isAlive();
    void takeDamage(H hunter);
}
