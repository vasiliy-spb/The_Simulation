package bio.world.factories;

import bio.world.entities.*;
import bio.world.map.WorldMap;
import bio.world.simulation.init.InitParams;
import bio.world.dialogs.Dialog;
import bio.world.dialogs.IntegerMinMaxDialog;

import java.util.Map;
import java.util.Random;

public class WorldMapFactory {
    private static final int MIN_HEIGHT = 5;
    private static final int MAX_HEIGHT = 35;
    private static final int MIN_WIDTH = 5;
    private static final int MAX_WIDTH = 50;
    private static final Map<Class<? extends Entity>, EntityFactory<? extends Entity>> factories = Map.of(
            Grass.class, new GrassFactory(),
            Rock.class, new RockFactory(),
            Tree.class, new TreeFactory(),
            Herbivore.class, new HerbivoreFactory(),
            Predator.class, new PredatorFactory()
    );

    public static WorldMap getInstance(int height, int width) {
        return new WorldMap(height, width);
    }

    public static WorldMap createWorldMapWithUserParams() {
        System.out.println("Введите стартовые параметры");
        String askHeightMessage = "Введите высоту карты (%d - %d): ".formatted(MIN_HEIGHT, MAX_HEIGHT);
        String askWidthMessage = "Введите ширину карты: (%d - %d)".formatted(MIN_WIDTH, MAX_WIDTH);
        String errorMessage = "Неправильный ввод.";

        int height = askIntegerParams(askHeightMessage, errorMessage, MIN_HEIGHT, MAX_HEIGHT);
        int width = askIntegerParams(askWidthMessage, errorMessage, MIN_WIDTH, MAX_WIDTH);

        return getInstance(height, width);
    }

    private static int askIntegerParams(String askMessage, String errorMessage, int minValue, int maxValue) {
        Dialog<Integer> integerDialog = new IntegerMinMaxDialog(askMessage, errorMessage, minValue, maxValue);
        return integerDialog.input();
    }

    public static WorldMap createWorldMapWithParams(InitParams initParams) {
        int height = initParams.height();
        int width = initParams.width();
        return getInstance(height, width);
    }

    public static WorldMap createWorldMap(InitParams initParams) {
        WorldMap worldMap = getInstance(initParams.height(), initParams.width());
        putEntities(worldMap, initParams);
        return worldMap;
    }

    private static void putEntities(WorldMap worldMap, InitParams initParams) {
        int countTrees = initParams.countTrees();
        putEntitiesOfType(Tree.class, countTrees, worldMap);
        int countRocks = initParams.countRocks();
        putEntitiesOfType(Rock.class, countRocks, worldMap);
        int countGrasses = initParams.countGrasses();
        putEntitiesOfType(Grass.class, countGrasses, worldMap);
        int countHerbivores = initParams.countHerbivores();
        putEntitiesOfType(Herbivore.class, countHerbivores, worldMap);
        int countPredators = initParams.countPredators();
        putEntitiesOfType(Predator.class, countPredators, worldMap);
    }

    private static <T extends Entity> void putEntitiesOfType(Class<T> type, int count, WorldMap worldMap) {
        while (count-- > 0) {
            Coordinates coordinates = createValidRandomCoordinates(worldMap);
            Entity entity = factories.get(type).createInstanceBy(coordinates);
            worldMap.addEntity(entity);
        }
    }

    private static Coordinates createValidRandomCoordinates(WorldMap worldMap) {
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        Random random = new Random();
        Coordinates coordinates;
        do {
            int row = random.nextInt(height);
            int column = random.nextInt(width);
            coordinates = new Coordinates(row, column);
        } while (worldMap.areBusy(coordinates));
        return coordinates;
    }
}
