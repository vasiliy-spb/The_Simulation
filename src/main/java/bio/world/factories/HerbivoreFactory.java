package bio.world.factories;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.entities.Herbivore;

public class HerbivoreFactory extends EntityFactory<Herbivore> {
    @Override
    public Herbivore createInstance(WorldMap worldMap) {
        Coordinates coordinates = createFreeCoordinates(worldMap);
        Herbivore herbivore = new Herbivore(coordinates);
        return herbivore;
    }
}
