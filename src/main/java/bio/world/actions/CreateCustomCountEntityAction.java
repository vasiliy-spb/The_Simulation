package bio.world.actions;

import bio.world.simulation.init.InitParams;
import bio.world.simulation.init.InitParamsHandler;
import bio.world.map.WorldMap;
import bio.world.dialogs.Dialog;
import bio.world.dialogs.IntegerMinMaxDialog;
import bio.world.entities.*;
import bio.world.factories.*;

import java.util.Map;

public class CreateCustomCountEntityAction implements Action {
    private final WorldMap worldMap;
    private final Map<Class<? extends Entity>, EntityFactory<? extends Entity>> factories;
    private final InitParamsHandler initParamsHandler;

    public CreateCustomCountEntityAction(WorldMap worldMap, InitParamsHandler initParamsHandler) {
        this.worldMap = worldMap;
        this.initParamsHandler = initParamsHandler;
        factories = Map.of(
                Grass.class, new GrassFactory(),
                Rock.class, new RockFactory(),
                Tree.class, new TreeFactory(),
                Herbivore.class, new HerbivoreFactory(),
                Predator.class, new PredatorFactory()
        );
    }

    @Override
    public void perform() {
        InitParams initParams = askInitParams();
        createRock(initParams.countRocks());
        createTree(initParams.countTrees());
        createGrass(initParams.countGrasses());
        createHerbivore(initParams.countHerbivores());
        createPredator(initParams.countPredators());
        initParamsHandler.saveInitParams(initParams);
        initParamsHandler.saveEntityPosition(worldMap);
    }

    private InitParams askInitParams() {
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        int availableCountEntities = height * width;
        String askMessage = "Введите количество %s (%d - %d): ";
        String errorMessage = "Неправильный ввод.";
        int countTrees = askIntegerParameter(askMessage.formatted("деревьев", 1, availableCountEntities - 4), errorMessage, 1, availableCountEntities - 4);
        availableCountEntities -= countTrees;
        int countRocks = askIntegerParameter(askMessage.formatted("камней", 1, availableCountEntities - 3), errorMessage, 1, availableCountEntities - 3);
        availableCountEntities -= countRocks;
        int countGrasses = askIntegerParameter(askMessage.formatted("травы", 1, availableCountEntities - 2), errorMessage, 1, availableCountEntities - 2);
        availableCountEntities -= countGrasses;
        int countHerbivores = askIntegerParameter(askMessage.formatted("травоядных", 1, availableCountEntities - 1), errorMessage, 1, availableCountEntities - 1);
        availableCountEntities -= countHerbivores;
        int countPredators = askIntegerParameter(askMessage.formatted("хищников", 1, availableCountEntities), errorMessage, 1, availableCountEntities);
        InitParams initParams = new InitParams(height, width, countTrees, countRocks, countGrasses, countHerbivores, countPredators);
        return initParams;
    }

    private static int askIntegerParameter(String askMessage, String errorMessage, int minValue, int maxValue) {
        Dialog<Integer> integerDialog = new IntegerMinMaxDialog(askMessage, errorMessage, minValue, maxValue);
        return integerDialog.input();
    }

    private void createPredator(int count) {
        while (count-- > 0) {
            Predator predator = (Predator) createEntity(Predator.class);
            worldMap.addEntity(predator);
        }
    }

    private void createHerbivore(int count) {
        while (count-- > 0) {
            Herbivore herbivore = (Herbivore) createEntity(Herbivore.class);
            worldMap.addEntity(herbivore);
        }
    }

    private void createTree(int count) {
        while (count-- > 0) {
            Tree tree = (Tree) createEntity(Tree.class);
            worldMap.addEntity(tree);
        }
    }

    private void createRock(int count) {
        while (count-- > 0) {
            Rock rock = (Rock) createEntity(Rock.class);
            worldMap.addEntity(rock);
        }
    }

    private void createGrass(int count) {
        while (count-- > 0) {
            Grass grass = (Grass) createEntity(Grass.class);
            worldMap.addEntity(grass);
        }
    }

    private Entity createEntity(Class<? extends Entity> eClass) {
        EntityFactory<? extends Entity> factory = factories.get(eClass);
        if (factory == null) {
            throw new IllegalArgumentException();
        }
        return factory.createInstance(worldMap);
    }
}
