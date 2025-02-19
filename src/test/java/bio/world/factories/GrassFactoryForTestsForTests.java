package bio.world.factories;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.entities.Grass;

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
