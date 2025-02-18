package bio.world.factories;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.entities.*;
import bio.world.render.Pictures;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WorldMapFactoryTest {
    public static WorldMap getRandomWorldMap() {
        Random random = new Random();
        int height = random.nextInt(3, 10);
        int width = random.nextInt(3, 10);
        return new WorldMap(height, width);
    }

    public static WorldMap getWorldMap(int height, int width) {
        return new WorldMap(height, width);
    }

    public static WorldMap createWorldMapByTemplate(String template) {
        String[][] worldMapMatrix = createWorldMapMatrix(template);
        int height = worldMapMatrix.length;
        int width = worldMapMatrix[0].length;
        WorldMap worldMap = getWorldMap(height, width);
        Map<Coordinates, Entity> entityMap = parseMatrix(worldMapMatrix);
        for (Entity entity : entityMap.values()) {
            if (entity instanceof Creature creature) {
                worldMap.addCreature(creature);
            } else if (entity instanceof StaticEntity staticEntity) {
                worldMap.addStaticEntity(staticEntity);
            }
        }
        return worldMap;
    }

    private static Map<Coordinates, Entity> parseMatrix(String[][] worldMapMatrix) {
        Map<Coordinates, Entity> entityMap = new HashMap<>();
        for (int i = 0; i < worldMapMatrix.length; i++) {
            for (int j = 0; j < worldMapMatrix[i].length; j++) {
                Coordinates coordinates = new Coordinates(i, j);
                String ceil = worldMapMatrix[i][j];
                if (ceil.equals(Pictures.GRASS.getValue())) {
                    entityMap.put(coordinates, new Grass(coordinates));
                } else if (ceil.equals(Pictures.STONE.getValue())) {
                    entityMap.put(coordinates, new Rock(coordinates));
                } else if (ceil.equals(Pictures.SPRUCE_TREE.getValue())) {
                    entityMap.put(coordinates, new Tree(coordinates));
                } else if (ceil.equals(Pictures.RABBIT.getValue())) {
                    entityMap.put(coordinates, new Herbivore(coordinates));
                } else if (ceil.equals(Pictures.TIGER.getValue())) {
                    entityMap.put(coordinates, new Predator(coordinates));
                }
            }
        }
        return entityMap;
    }

    private static String[][] createWorldMapMatrix(String template) {
        String[] lines = template.split("\n");
        String[][] worldMapMatrix = new String[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            worldMapMatrix[i] = lines[i].trim().split(" ");
        }
        return worldMapMatrix;
    }
}
