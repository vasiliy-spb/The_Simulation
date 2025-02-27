package bio.world.entities.temporary;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;

public abstract class TemporaryEntity extends Entity {
    private static final int MODULE = 1_000_000;
    protected int initialTick;
    protected int lastTick;

    public TemporaryEntity(Coordinates coordinates, int initialTick, int lifeTime) {
        super(coordinates);
        this.initialTick = initialTick;
        this.lastTick = (initialTick + lifeTime) % MODULE;
    }

    public boolean isLifeTimeOver(int tick) {
        return this.lastTick <= tick;
    }
}
