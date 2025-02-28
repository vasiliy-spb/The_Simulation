package bio.world.actions;

import bio.world.entities.Coordinates;
import bio.world.entities.regular.Grass;
import bio.world.entities.regular.Herbivore;
import bio.world.entities.regular.Huntsman;
import bio.world.entities.regular.Predator;
import bio.world.entities.statical.Rock;
import bio.world.entities.statical.Tree;
import bio.world.simulation.init.InitParamsHandler;
import bio.world.map.WorldMap;
import bio.world.entities.*;
import bio.world.factories.*;

import java.util.Map;
import java.util.Optional;

public class CreateSavedCountEntityAction implements Action {
    private final WorldMap worldMap;
    private final InitParamsHandler initParamsHandler;
    private final Map<Class<? extends Entity>, EntityFactory<? extends Entity>> factories;

    public CreateSavedCountEntityAction(WorldMap worldMap, InitParamsHandler initParamsHandler) {
        this.worldMap = worldMap;
        this.initParamsHandler = initParamsHandler;
        this.factories = Map.of(
                Grass.class, new GrassFactory(),
                Rock.class, new RockFactory(),
                Tree.class, new TreeFactory(),
                Herbivore.class, new HerbivoreFactory(),
                Predator.class, new PredatorFactory(),
                Huntsman.class, new HuntsmanFactory()
        );
    }

    @Override
    public void perform() {
        Optional<Map<Integer, Class<? extends Entity>>> startingPositionContainer = initParamsHandler.getSavedPosition();

        if (startingPositionContainer.isEmpty()) {
            throw new IllegalStateException();
        }

        int width = worldMap.getWidth();
        Map<Integer, Class<? extends Entity>> startingPosition = startingPositionContainer.get();
        for (Map.Entry<Integer, Class<? extends Entity>> entry : startingPosition.entrySet()) {

            int key = entry.getKey();
            int row = key / width;
            int column = key % width;
            Coordinates coordinates = new Coordinates(row, column);
            Class<? extends Entity> eClass = entry.getValue();

            EntityFactory<? extends Entity> factory = factories.get(eClass);

            if (factory == null) {
                throw new IllegalArgumentException();
            }

            Entity entity = factory.createInstanceBy(coordinates);
            worldMap.addEntity(entity);
        }
    }
}
