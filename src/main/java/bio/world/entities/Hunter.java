package bio.world.entities;

public interface Hunter<P extends Entity & Prey<? extends Hunter<P>>> {
    int getDamage();

    void attack(P prey);
}
