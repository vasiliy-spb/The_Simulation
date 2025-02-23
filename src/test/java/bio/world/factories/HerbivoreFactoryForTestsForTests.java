package bio.world.factories;

import bio.world.entities.Coordinates;
import bio.world.map.WorldMap;
import bio.world.entities.Herbivore;

public class HerbivoreFactoryForTestsForTests extends EntityFactoryForTests<Herbivore> {
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
