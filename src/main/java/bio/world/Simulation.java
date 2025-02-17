package bio.world;

import bio.world.entities.*;
import bio.world.factories.*;
import bio.world.render.ConsoleMapRender;
import bio.world.render.WorldMapRender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Simulation {
    private final WorldMap worldMap;
    private final MoveCounter moveCounter;
    private final WorldMapRender worldMapRender;
    private final List<Actions> actionsList;
    private final Map<Class<? extends Entity>, EntityFactory<? extends Entity>> factories;

    public Simulation() {
        this.worldMap = WorldMapFactory.getRandomWorldMap();
        this.moveCounter = new MoveCounter();
        this.worldMapRender = new ConsoleMapRender(worldMap);
        this.actionsList = new ArrayList<>();
        factories = Map.of(
                Grass.class, new GrassFactory(),
                Rock.class, new RockFactory(),
                Tree.class, new TreeFactory(),
                Herbivore.class, new HerbivoreFactory()
//                Predator.class, new PredatorFactory(),
//                Huntsman.class, new HuntsmanFactory(),
        );
    }

    public void start() {
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        int maxEntityNumber = height * width;
        for (int i = 0; i < maxEntityNumber / 4; i++) {
            createGrass();
        }
        for (int i = 0; i < maxEntityNumber / 4; i++) {
            createRock();
        }
        for (int i = 0; i < maxEntityNumber / 4; i++) {
            createTree();
        }
        for (int i = 0; i < maxEntityNumber / 4; i++) {
            createHerbivore();
        }
        worldMapRender.renderMap();
    }

    private void createHerbivore() {
        Herbivore herbivore = (Herbivore) createEntity(Herbivore.class);
        worldMap.addCreature(herbivore);
    }

    private void createTree() {
        Tree tree = (Tree) createEntity(Tree.class);
        worldMap.addStaticEntity(tree);
    }

    private void createRock() {
        Rock rock = (Rock) createEntity(Rock.class);
        worldMap.addStaticEntity(rock);
    }

    private void createGrass() {
        Grass grass = (Grass) createEntity(Grass.class);
        worldMap.addStaticEntity(grass);
    }

    private Entity createEntity(Class<? extends Entity> eClass) {
        EntityFactory<? extends Entity> factory = factories.get(eClass);
        if (factory == null) {
            throw new IllegalArgumentException();
        }
        return factory.createInstance(worldMap);
    }
}
