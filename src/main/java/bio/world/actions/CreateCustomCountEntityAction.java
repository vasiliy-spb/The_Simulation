package bio.world.actions;

import bio.world.WorldMap;
import bio.world.dialogs.Dialog;
import bio.world.dialogs.IntegerMinMaxDialog;
import bio.world.entities.*;
import bio.world.factories.*;

import java.util.Map;

public class CreateCustomCountEntityAction implements Action {
    private final WorldMap worldMap;
    private final Map<Class<? extends Entity>, EntityFactory<? extends Entity>> factories;

    public CreateCustomCountEntityAction(WorldMap worldMap) {
        this.worldMap = worldMap;
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
    }

    private InitParams askInitParams() {
        int availableCountEntities = worldMap.getHeight() * worldMap.getWidth();
        String askMessage = "Введите количество %s (%d - %d): ";
        String errorMessage = "Неправильный ввод.";
        int countTrees = askIntegerParams(askMessage.formatted("деревьев", 1, availableCountEntities - 4), errorMessage, 1, availableCountEntities - 4);
        availableCountEntities -= countTrees;
        int countRocks = askIntegerParams(askMessage.formatted("камней", 1, availableCountEntities - 3), errorMessage, 1, availableCountEntities - 3);
        availableCountEntities -= countRocks;
        int countGrasses = askIntegerParams(askMessage.formatted("травы", 1, availableCountEntities - 2), errorMessage, 1, availableCountEntities - 2);
        availableCountEntities -= countGrasses;
        int countHerbivores = askIntegerParams(askMessage.formatted("травоядных", 1, availableCountEntities - 1), errorMessage, 1, availableCountEntities - 1);
        availableCountEntities -= countHerbivores;
        int countPredators = askIntegerParams(askMessage.formatted("хищников", 1, availableCountEntities), errorMessage, 1, availableCountEntities);
        InitParams initParams = new InitParams(countTrees, countRocks, countGrasses, countHerbivores, countPredators);
        return initParams;
    }

    private static int askIntegerParams(String askMessage, String errorMessage, int minValue, int maxValue) {
        Dialog<Integer> integerDialog = new IntegerMinMaxDialog(askMessage, errorMessage, minValue, maxValue);
        return integerDialog.input();
    }

    private void createPredator(int count) {
        while (count-- > 0) {
            Predator predator = (Predator) createEntity(Predator.class);
            worldMap.addCreature(predator);
        }
    }

    private void createHerbivore(int count) {
        while (count-- > 0) {
            Herbivore herbivore = (Herbivore) createEntity(Herbivore.class);
            worldMap.addCreature(herbivore);
        }
    }

    private void createTree(int count) {
        while (count-- > 0) {
            Tree tree = (Tree) createEntity(Tree.class);
            worldMap.addStaticEntity(tree);
        }
    }

    private void createRock(int count) {
        while (count-- > 0) {
            Rock rock = (Rock) createEntity(Rock.class);
            worldMap.addStaticEntity(rock);
        }
    }

    private void createGrass(int count) {
        while (count-- > 0) {
            Grass grass = (Grass) createEntity(Grass.class);
            worldMap.addStaticEntity(grass);
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
