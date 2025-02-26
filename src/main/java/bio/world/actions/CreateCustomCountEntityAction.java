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
    private static final int MIN_NUMBER_OF_ENTITY = 1;
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

        createEntities(Tree.class, initParams.countTrees());
        createEntities(Rock.class, initParams.countRocks());
        createEntities(Grass.class, initParams.countGrasses());
        createEntities(Herbivore.class, initParams.countHerbivores());
        createEntities(Predator.class, initParams.countPredators());

        initParamsHandler.saveInitParams(initParams);
        initParamsHandler.saveEntityPosition(worldMap);
    }

    private InitParams askInitParams() {
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        int availableCountEntities = height * width;
        String askMessage = "Введите количество %s (%d - %d): ";
        String errorMessage = "Неправильный ввод.";

        int countTrees = askIntegerParameter(askMessage.formatted("деревьев", MIN_NUMBER_OF_ENTITY, availableCountEntities - 4), errorMessage, MIN_NUMBER_OF_ENTITY, availableCountEntities - 4);
        availableCountEntities -= countTrees;
        int countRocks = askIntegerParameter(askMessage.formatted("камней", MIN_NUMBER_OF_ENTITY, availableCountEntities - 3), errorMessage, MIN_NUMBER_OF_ENTITY, availableCountEntities - 3);
        availableCountEntities -= countRocks;
        int countGrasses = askIntegerParameter(askMessage.formatted("травы", MIN_NUMBER_OF_ENTITY, availableCountEntities - 2), errorMessage, MIN_NUMBER_OF_ENTITY, availableCountEntities - 2);
        availableCountEntities -= countGrasses;
        int countHerbivores = askIntegerParameter(askMessage.formatted("травоядных", MIN_NUMBER_OF_ENTITY, availableCountEntities - 1), errorMessage, MIN_NUMBER_OF_ENTITY, availableCountEntities - 1);
        availableCountEntities -= countHerbivores;
        int countPredators = askIntegerParameter(askMessage.formatted("хищников", MIN_NUMBER_OF_ENTITY, availableCountEntities), errorMessage, MIN_NUMBER_OF_ENTITY, availableCountEntities);

        InitParams initParams = new InitParams(height, width, countTrees, countRocks, countGrasses, countHerbivores, countPredators);
        return initParams;
    }

    private static int askIntegerParameter(String askMessage, String errorMessage, int minValue, int maxValue) {
        Dialog<Integer> integerDialog = new IntegerMinMaxDialog(askMessage, errorMessage, minValue, maxValue);
        return integerDialog.input();
    }

    private void createEntities(Class<? extends Entity> eClass, int count) {
        EntityFactory<? extends Entity> factory = factories.get(eClass);
        if (factory == null) {
            throw new IllegalArgumentException();
        }
        while (count-- > 0) {
            Entity entity = factory.createInstance(worldMap);
            worldMap.addEntity(entity);
        }
    }
}
