package bio.world.factories;

import bio.world.entities.Coordinates;
import bio.world.entities.regular.Huntsman;

public class HuntsmanFactory extends EntityFactory<Huntsman> {
    @Override
    public Huntsman createInstanceBy(Coordinates coordinates) {
        Huntsman huntsman = new Huntsman(coordinates);
        return huntsman;
    }
}
