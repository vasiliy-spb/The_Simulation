package bio.world.factories;

import bio.world.WorldMap;

import java.util.Random;

public class WorldMapFactory {
    public static WorldMap getRandomWorldMap() {
        Random random = new Random();
        int height = random.nextInt(3, 10);
        int width = random.nextInt(3, 10);
        return new WorldMap(height, width);
    }
}
