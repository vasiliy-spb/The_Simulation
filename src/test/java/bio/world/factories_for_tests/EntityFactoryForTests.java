package bio.world.factories_for_tests;

import bio.world.entities.Coordinates;
import bio.world.map.WorldMap;

import java.util.Random;

public abstract class EntityFactoryForTests<T> {
    protected final Random random = new Random();

    public abstract T createInstance(WorldMap worldMap);

    protected Coordinates createFreeCoordinates(WorldMap worldMap) {
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
    public abstract T createInstanceByCoordinate(WorldMap worldMap, Coordinates coordinates);
}
