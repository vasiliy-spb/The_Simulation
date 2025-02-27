package bio.world.factories_for_tests;

import bio.world.entities.Coordinates;
import bio.world.map.WorldMap;
import bio.world.entities.regular.Grass;

public class GrassFactoryForTestsForTests extends EntityFactoryForTests<Grass> {
    @Override
    public Grass createInstance(WorldMap worldMap) {
        Coordinates coordinates = createFreeCoordinates(worldMap);
        Grass grass = new Grass(coordinates);
        return grass;
    }

    @Override
    public Grass createInstanceByCoordinate(WorldMap worldMap, Coordinates coordinates) {
        Grass grass = new Grass(coordinates);
        return grass;
    }
}
