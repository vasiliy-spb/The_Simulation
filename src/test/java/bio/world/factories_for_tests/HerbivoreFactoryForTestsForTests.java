package bio.world.factories_for_tests;

import bio.world.entities.Coordinates;
import bio.world.map.WorldMap;
import bio.world.entities.regular.Herbivore;

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
