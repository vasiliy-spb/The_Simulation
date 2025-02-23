package bio.world.factories;

import bio.world.entities.Coordinates;
import bio.world.map.WorldMap;
import bio.world.entities.*;
import bio.world.render.Pictures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WorldMapFactoryForTests {
    public static WorldMap getRandomWorldMap() {
        Random random = new Random();
        int height = random.nextInt(3, 10);
        int width = random.nextInt(3, 10);
        return new WorldMap(height, width);
    }

    public static WorldMap getWorldMap(int height, int width) {
        return new WorldMap(height, width);
    }

    public static WorldMap createWorldMapByTemplate(String filePath) {
        String template = readWorldMapTemplate(filePath);
        String[][] worldMapMatrix = createWorldMapMatrix(template);
        WorldMap worldMap = getWorldMapByWorldMapMatrix(worldMapMatrix);
        Map<Coordinates, Entity> entities = parseMatrix(worldMapMatrix);
        putEntities(entities, worldMap);
        return worldMap;
    }

    private static WorldMap getWorldMapByWorldMapMatrix(String[][] worldMapMatrix) {
        int height = worldMapMatrix.length;
        int width = worldMapMatrix[0].length;
        return getWorldMap(height, width);
    }

    private static void putEntities(Map<Coordinates, Entity> entities, WorldMap worldMap) {
        for (Entity entity : entities.values()) {
            worldMap.addEntity(entity);
        }
    }

    private static String readWorldMapTemplate(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder template = new StringBuilder();
            while (reader.ready()) {
                template.append(reader.readLine()).append("\n");
            }
            return template.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
