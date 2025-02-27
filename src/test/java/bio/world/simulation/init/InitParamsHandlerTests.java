package bio.world.simulation.init;

import bio.world.entities.*;
import bio.world.entities.regular.Grass;
import bio.world.entities.regular.Herbivore;
import bio.world.entities.regular.Predator;
import bio.world.entities.statical.Rock;
import bio.world.entities.statical.Tree;
import bio.world.factories.*;
import bio.world.factories_for_tests.WorldMapFactoryForTests;
import bio.world.map.WorldMap;
import bio.world.render.ConsoleMapRender;
import bio.world.render.WorldMapRender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InitParamsHandlerTests {
    @Test
    @DisplayName("Test InitParamsHandler on 1000 random templates")
    public void checkTestcase01() {
        Map<Class<? extends Entity>, EntityFactory<? extends Entity>> factories = Map.of(
                Grass.class, new GrassFactory(),
                Rock.class, new RockFactory(),
                Tree.class, new TreeFactory(),
                Herbivore.class, new HerbivoreFactory(),
                Predator.class, new PredatorFactory()
        );
        Random random = new Random();
        boolean allMatched = true;
        for (int i = 0; i < 1000; i++) {
            InitParams initParams = generateInitParams(random);
            WorldMap worldMap1 = WorldMapFactoryForTests.createWorldMapWithInitParams(initParams);

            InitParamsHandler initParamsHandler = new InitParamsHandler();
            initParamsHandler.saveInitParams(initParams);
            initParamsHandler.saveEntityPosition(worldMap1);

            InitParams savedInitParams = initParamsHandler.getSavedInitParams().get();
            WorldMap worldMap2 = WorldMapFactoryForTests.getWorldMap(savedInitParams.height(), savedInitParams.width());

            int width = worldMap2.getWidth();
            if (!initParamsHandler.hasSavedData()) {
                System.out.println("Ничего не сохранено");
                return;
            }
            Map<Integer, Class<? extends Entity>> startingPosition = initParamsHandler.getSavedPosition().get();
            for (Map.Entry<Integer, Class<? extends Entity>> entry : startingPosition.entrySet()) {
                int key = entry.getKey();
                int row = key / width;
                int column = key % width;
                Coordinates coordinates = new Coordinates(row, column);
                Class<? extends Entity> eClass = entry.getValue();
                Entity entity = factories.get(eClass).createInstanceBy(coordinates);
                worldMap2.addEntity(entity);
            }

            for (Entity entity1 : worldMap1.getAllEntities()) {
                try {
                    Entity entity2 = worldMap2.getEntityByCoordinates(entity1.getCoordinates());
                    allMatched &= entity1.getClass().getSimpleName().equals(entity2.getClass().getSimpleName());
                } catch (IllegalArgumentException ignored) {
                    allMatched = false;
                    System.out.println(entity1.getCoordinates());
                }
            }

            if (!allMatched) {
                WorldMapRender mapRender1 = new ConsoleMapRender(worldMap1);
                WorldMapRender mapRender2 = new ConsoleMapRender(worldMap2);
                System.out.println("Map before:");
                mapRender1.renderMap();
                System.out.println("Map after:");
                mapRender2.renderMap();
            }
        }
        assertTrue(allMatched);
    }

    private static InitParams generateInitParams(Random random) {
        int height = random.nextInt(5, 35);
        int width = random.nextInt(5, 50);
        int availableCountEntities = height * width;

        int countTrees = random.nextInt(1, availableCountEntities - 4);
        availableCountEntities -= countTrees;
        int countRocks = random.nextInt(1, availableCountEntities - 3);
        availableCountEntities -= countRocks;
        int countGrasses = random.nextInt(1, availableCountEntities - 2);
        availableCountEntities -= countGrasses;
        int countHerbivores = random.nextInt(1, availableCountEntities - 1);
        availableCountEntities -= countHerbivores;
        int countPredators = random.nextInt(1, availableCountEntities);
        InitParams initParams = new InitParams(height, width, countTrees, countRocks, countGrasses, countHerbivores, countPredators);
        return initParams;
    }
}
