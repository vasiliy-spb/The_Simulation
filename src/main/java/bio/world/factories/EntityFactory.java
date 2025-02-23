package bio.world.factories;

import bio.world.entities.Coordinates;
import bio.world.map.WorldMap;

import java.util.Random;

public abstract class EntityFactory<T> {
    protected final Random random = new Random();

    public T createInstance(WorldMap worldMap) {
        Coordinates coordinates = createFreeCoordinates(worldMap);
        return createInstanceBy(coordinates);
    }

    public abstract T createInstanceBy(Coordinates coordinates);

    private Coordinates createFreeCoordinates(WorldMap worldMap) {
        Coordinates coordinates;
        do {
            int height = worldMap.getHeight();
            int width = worldMap.getWidth();
            int row = random.nextInt(height);
            int column = random.nextInt(width);
            coordinates = new Coordinates(row, column);
        } while (worldMap.areBusy(coordinates));
        return coordinates;
    }
}
