package bio.world.factories;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.entities.Grass;

public class GrassFactory extends EntityFactory<Grass> {
    @Override
    public Grass createInstance(WorldMap worldMap) {
        Coordinates coordinates = createFreeCoordinates(worldMap);
        Grass grass = new Grass(coordinates);
        return grass;
    }

    public Grass createInstanceBy(Coordinates coordinates) {
        Grass grass = new Grass(coordinates);
        return grass;
    }
}
