package bio.world.actions_for_tests;

import bio.world.actions.Action;
import bio.world.entities.regular.Grass;
import bio.world.entities.regular.Herbivore;
import bio.world.entities.regular.Predator;
import bio.world.entities.statical.Rock;
import bio.world.entities.statical.Tree;
import bio.world.map.WorldMap;
import bio.world.entities.*;
import bio.world.factories.*;

import java.util.Map;

public class CreateFixedCountEntityAction implements Action {
    private final WorldMap worldMap;
    private final Map<Class<? extends Entity>, EntityFactory<? extends Entity>> factories;
    private final int countEntity;
    public CreateFixedCountEntityAction(WorldMap worldMap, int countEntity) {
        this.worldMap = worldMap;
        this.countEntity = countEntity;
        factories = Map.of(
                Grass.class, new GrassFactory(),
                Rock.class, new RockFactory(),
                Tree.class, new TreeFactory(),
                Herbivore.class, new HerbivoreFactory(),
                Predator.class, new PredatorFactory()
        );
    }
    public CreateFixedCountEntityAction(WorldMap worldMap) {
        this.worldMap = worldMap;
        factories = Map.of(
                Grass.class, new GrassFactory(),
                Rock.class, new RockFactory(),
                Tree.class, new TreeFactory(),
                Herbivore.class, new HerbivoreFactory(),
                Predator.class, new PredatorFactory()
        );
        this.countEntity = worldMap.getHeight() * worldMap.getWidth() / (factories.size() + 1);
    }

    @Override
    public void perform() {
        createGrass(countEntity);
        createRock(countEntity);
        createTree(countEntity);
        createHerbivore(countEntity);
        createPredator(countEntity);
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
