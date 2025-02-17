package bio.world.factories;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.entities.Rock;

public class RockFactory extends EntityFactory<Rock> {
    @Override
    public Rock createInstance(WorldMap worldMap) {
        Coordinates coordinates = createFreeCoordinates(worldMap);
        Rock rock = new Rock(coordinates);
        return rock;
    }
}
