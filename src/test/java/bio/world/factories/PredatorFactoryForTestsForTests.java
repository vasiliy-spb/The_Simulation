package bio.world.factories;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.entities.Predator;

public class PredatorFactoryForTestsForTests extends EntityFactoryForTests<Predator> {
    @Override
    public Predator createInstance(WorldMap worldMap) {
        Coordinates coordinates = createFreeCoordinates(worldMap);
        Predator predator = new Predator(coordinates);
        return predator;
    }

    @Override
    public Predator createInstanceByCoordinate(WorldMap worldMap, Coordinates coordinates) {
        Predator predator = new Predator(coordinates);
        return predator;
    }
}
