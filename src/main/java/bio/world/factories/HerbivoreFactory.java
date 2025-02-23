package bio.world.factories;

import bio.world.entities.Coordinates;
import bio.world.entities.Herbivore;

public class HerbivoreFactory extends EntityFactory<Herbivore> {
    @Override
    public Herbivore createInstanceBy(Coordinates coordinates) {
        Herbivore herbivore = new Herbivore(coordinates);
        return herbivore;
    }
}
