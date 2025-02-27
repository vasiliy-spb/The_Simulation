package bio.world.factories_for_tests;

import bio.world.entities.Coordinates;
import bio.world.map.WorldMap;
import bio.world.entities.Rock;

public class RockFactoryForTestsForTests extends EntityFactoryForTests<Rock> {
    @Override
    public Rock createInstance(WorldMap worldMap) {
        Coordinates coordinates = createFreeCoordinates(worldMap);
        Rock rock = new Rock(coordinates);
        return rock;
    }

    @Override
    public Rock createInstanceByCoordinate(WorldMap worldMap, Coordinates coordinates) {
        Rock rock = new Rock(coordinates);
        return rock;
    }
}
