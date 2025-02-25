package bio.world.simulation.init;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.map.WorldMap;

import java.util.*;

public class InitParamsHandler {
    private static final InitParams EMPTY_INIT_PARAMS = new InitParams(0, 0, 0, 0, 0, 0, 0);
    private InitParams savedInitParams;
    private Map<Integer, Class<? extends Entity>> savedEntityPositions;

    public InitParamsHandler() {
        this.savedInitParams = EMPTY_INIT_PARAMS;
        this.savedEntityPositions = new HashMap<>();
    }

    public void saveInitParams(InitParams initParams) {
        this.savedInitParams = initParams;
    }

    public Optional<InitParams> getSavedInitParams() {
        if (isInitParamsEmpty()) {
            return Optional.empty();
        }
        return Optional.of(savedInitParams);
    }

    private boolean isInitParamsEmpty() {
        return savedInitParams.equals(EMPTY_INIT_PARAMS);
    }

    public void saveEntityPosition(WorldMap worldMap) {
        this.savedEntityPositions = new HashMap<>();
        int width = worldMap.getWidth();
        List<Entity> entities = worldMap.getAllEntities();
        for (Entity entity : entities) {
            Coordinates coordinates = entity.getCoordinates();
            int key = coordinates.row() * width + coordinates.column();
            this.savedEntityPositions.put(key, entity.getClass());
        }
    }

    public Optional<Map<Integer, Class<? extends Entity>>> getSavedPosition() {
        if (savedEntityPositions.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new HashMap<>(savedEntityPositions));
    }

    public boolean hasSavedData() {
        return !isInitParamsEmpty() && !savedEntityPositions.isEmpty();
    }
}
