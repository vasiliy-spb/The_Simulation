package bio.world.factories_for_tests;

import bio.world.entities.Coordinates;
import bio.world.factories.*;
import bio.world.map.WorldMap;
import bio.world.entities.*;
import bio.world.simulation.init.InitParams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static bio.world.render.ConsoleEntityIcons.*;

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
                switch (ceil) {
                    case GRASS_ICON -> entityMap.put(coordinates, new Grass(coordinates));
                    case ROCK_ICON -> entityMap.put(coordinates, new Rock(coordinates));
                    case TREE_ICON -> entityMap.put(coordinates, new Tree(coordinates));
                    case HERBIVORE_ICON -> entityMap.put(coordinates, new Herbivore(coordinates));
                    case PREDATOR_ICON -> entityMap.put(coordinates, new Predator(coordinates));
                    case HUNTSMEN_ICON -> entityMap.put(coordinates, new Huntsmen(coordinates));
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

    private static final Map<Class<? extends Entity>, EntityFactory<? extends Entity>> factories = Map.of(
            Grass.class, new GrassFactory(),
            Rock.class, new RockFactory(),
            Tree.class, new TreeFactory(),
            Herbivore.class, new HerbivoreFactory(),
            Predator.class, new PredatorFactory()
    );
    public static WorldMap createWorldMapWithInitParams(InitParams initParams) {
        WorldMap worldMap = getWorldMap(initParams.height(), initParams.width());
        putRandomEntities(initParams.countTrees(), Tree.class, worldMap);
        putRandomEntities(initParams.countRocks(), Rock.class, worldMap);
        putRandomEntities(initParams.countGrasses(), Grass.class, worldMap);
        putRandomEntities(initParams.countHerbivores(), Herbivore.class, worldMap);
        putRandomEntities(initParams.countPredators(), Predator.class, worldMap);
        return worldMap;
    }

    private static void putRandomEntities(int count, Class<? extends Entity> eClass, WorldMap worldMap) {
        Random random = new Random();
        Set<Coordinates> busyCoordinates = worldMap.getBusyCoordinates();
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        Coordinates coordinates;
        while (count-- > 0) {
            do {
                int row = random.nextInt(height);
                int column = random.nextInt(width);
                coordinates = new Coordinates(row, column);
            } while (busyCoordinates.contains(coordinates));
            Entity entity = factories.get(eClass).createInstanceBy(coordinates);
            worldMap.addEntity(entity);
        }
    }
}
