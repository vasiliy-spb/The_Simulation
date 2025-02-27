package bio.world.entities;

public interface Hunter<P> {
    int getDamage();

    void attack(P prey);
}
