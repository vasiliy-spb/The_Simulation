package bio.world.entities.statical.trap;

import bio.world.entities.regular.Creature;

import java.util.Optional;

public class TrapStatusCaught implements TrapStatus {
    private Creature capturedCreature;

    public TrapStatusCaught(Creature creature) {
        this.capturedCreature = creature;
    }

    @Override
    public void capture(Creature creature) {
        this.capturedCreature = creature;
    }

    @Override
    public boolean hasCapturedCreature() {
        return true;
    }

    @Override
    public Optional<Creature> getCapturedCreature() {
        return Optional.of(capturedCreature);
    }
}
