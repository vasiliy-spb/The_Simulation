package bio.world.entities.statical.trap;

import bio.world.entities.regular.Creature;

import java.util.Optional;

public interface TrapStatus {
    void capture(Creature creature);

    boolean hasCapturedCreature();

    Optional<Creature> getCapturedCreature();
}
