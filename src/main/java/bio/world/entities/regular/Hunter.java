package bio.world.entities.regular;

public interface Hunter<P> {
    int getDamage();

    void attack(P prey);
}
