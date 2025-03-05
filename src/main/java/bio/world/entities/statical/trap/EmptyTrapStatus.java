package bio.world.entities.statical.trap;

import bio.world.entities.regular.Creature;

import java.util.Optional;

public class EmptyTrapStatus implements TrapStatus {
    @Override
    public void capture(Creature creature) {
    }

    @Override
    public boolean hasCapturedCreature() {
        return false;
    }

    @Override
    public Optional<Creature> getCapturedCreature() {
        return Optional.empty();
    }
}
