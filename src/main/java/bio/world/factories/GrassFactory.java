package bio.world.factories;

import bio.world.entities.Coordinates;
import bio.world.entities.regular.Grass;

public class GrassFactory extends EntityFactory<Grass> {
    @Override
    public Grass createInstanceBy(Coordinates coordinates) {
        Grass grass = new Grass(coordinates);
        return grass;
    }
}
