package bio.world.factories;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.entities.Herbivore;

public class HerbivoreFactory extends EntityFactory<Herbivore> {
    @Override
    public Herbivore createInstance(WorldMap worldMap) {
        Coordinates coordinates;
        do {
            int height = worldMap.getHeight();
            int width = worldMap.getWidth();
            int row = random.nextInt(height + 1);
            int column = random.nextInt(width + 1);
            coordinates = new Coordinates(row, column);
        } while (worldMap.areBusy(coordinates));
        Herbivore herbivore = new Herbivore(coordinates);
        return herbivore;
    }
}
