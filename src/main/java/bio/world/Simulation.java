package bio.world;

import bio.world.entities.*;
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
                Grass.class, new GrassFactory()
//                Herbivore.class, new HerbivoreFactory(),
//                Predator.class, new PredatorFactory(),
//                Rock.class, new RockFactory(),
//                Huntsman.class, new HuntsmanFactory(),
//                Tree.class, new TreeFactory()
        );
    }
    
    public void start() {
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        int maxEntityNumber = height * width;
        for (int i = 0; i < maxEntityNumber / 2; i++) {
            createGrass();
        }
        worldMapRender.renderMap();
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
