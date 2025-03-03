package bio.world.entities.statical.trap;

import bio.world.entities.Coordinates;
import bio.world.entities.regular.Creature;
import bio.world.entities.regular.Huntsman;
import bio.world.entities.statical.StaticEntity;

import java.util.Optional;

public class Trap extends StaticEntity {
    private TrapStatus trapStatus;
    private final Huntsman owner;
    public Trap(Coordinates coordinates, Huntsman owner) {
        super(coordinates);
        this.owner = owner;
        this.trapStatus = new TrapStatusEmpty();
    }

    public void capture(Creature creature) {
        this.trapStatus = new TrapStatusCaught(creature);
    }

    public boolean hasCapturedCreature() {
        return trapStatus.hasCapturedCreature();
    }

    public Optional<Creature> getCapturedCreature() {
        return trapStatus.getCapturedCreature();
    }

    public Huntsman getOwner() {
        return this.owner;
    }
}
