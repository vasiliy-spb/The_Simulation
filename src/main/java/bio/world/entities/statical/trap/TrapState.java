package bio.world.entities.statical.trap;

import bio.world.entities.regular.Creature;

import java.util.Optional;

public interface TrapState {
    void capture(Creature creature);

    boolean hasCapturedCreature();

    Optional<Creature> getCapturedCreature();
}
