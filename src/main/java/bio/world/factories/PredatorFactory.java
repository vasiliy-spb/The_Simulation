package bio.world.factories;

import bio.world.entities.Coordinates;
import bio.world.entities.Predator;

public class PredatorFactory extends EntityFactory<Predator> {
    @Override
    public Predator createInstanceBy(Coordinates coordinates) {
        Predator predator = new Predator(coordinates);
        return predator;
    }
}
