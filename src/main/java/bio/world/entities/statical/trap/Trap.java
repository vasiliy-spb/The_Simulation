package bio.world.entities.statical.trap;

import bio.world.entities.Coordinates;
import bio.world.entities.regular.Creature;
import bio.world.entities.regular.Huntsman;
import bio.world.entities.statical.StaticEntity;

import java.util.Optional;

public class Trap extends StaticEntity {
    private TrapState trapState;
    private final Huntsman owner;
    public Trap(Coordinates coordinates, Huntsman owner) {
        super(coordinates);
        this.owner = owner;
        this.trapState = new EmptyTrapState();
    }

    public void capture(Creature creature) {
        this.trapState = new CaughtTrapState(creature);
    }

    public boolean hasCapturedCreature() {
        return trapState.hasCapturedCreature();
    }

    public Optional<Creature> getCapturedCreature() {
        return trapState.getCapturedCreature();
    }

    public Huntsman getOwner() {
        return this.owner;
    }
}
