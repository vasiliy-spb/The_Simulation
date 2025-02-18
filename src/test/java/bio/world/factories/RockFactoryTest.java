package bio.world.factories;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.entities.Rock;

public class RockFactoryTest extends TestEntityFactory<Rock> {
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
