package bio.world.entities.statical;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;

public abstract class StaticEntity extends Entity {
    public StaticEntity(Coordinates coordinates) {
        super(coordinates);
    }
}
