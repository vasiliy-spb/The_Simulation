package bio.world.entities;

public interface Hunter<P extends Prey<? extends Hunter<P>>> {
    int getDamage();
}
