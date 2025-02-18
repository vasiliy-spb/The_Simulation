package factories;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.entities.Herbivore;

public class HerbivoreFactoryTest extends TestEntityFactory<Herbivore> {
    @Override
    public Herbivore createInstance(WorldMap worldMap) {
        Coordinates coordinates = createFreeCoordinates(worldMap);
        Herbivore herbivore = new Herbivore(coordinates);
        return herbivore;
    }

    @Override
    public Herbivore createInstanceByCoordinate(WorldMap worldMap, Coordinates coordinates) {
        Herbivore herbivore = new Herbivore(coordinates);
        return herbivore;
    }
}
