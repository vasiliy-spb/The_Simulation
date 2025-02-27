package bio.world.entities.temporary;

import bio.world.entities.Coordinates;

public class Flash extends TemporaryEntity{
    private static final int LIFE_TIME = 1;
    public Flash(Coordinates coordinates, int initialTick) {
        super(coordinates, initialTick, LIFE_TIME);
    }
}
