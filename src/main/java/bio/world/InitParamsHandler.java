package bio.world;

import bio.world.entities.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class InitParamsHandler {
    private InitParams initParams;
    private Map<Integer, Class<? extends Entity>> position;

    public void saveInitParams(InitParams initParams) {
        this.initParams = initParams;
    }

    public Optional<InitParams> getInitParams() {
        return Optional.ofNullable(initParams);
    }

    public void saveStartingPosition(WorldMap worldMap) {
        position = new HashMap<>();
        int height = worldMap.getHeight();
        Set<Coordinates> occupiedCoordinates = worldMap.getBusyCoordinates();
        for (Coordinates coordinates : occupiedCoordinates) {
            Entity entity = worldMap.getEntityByCoordinates(coordinates);
            int key = coordinates.row() * height + coordinates.column();
            position.put(key, entity.getClass());
        }
    }

    public Optional<Map<Integer, Class<? extends Entity>>> getPosition() {
        return Optional.ofNullable(position);
    }

    public boolean hasSavedParams() {
        return this.initParams != null && this.position != null;
    }
}
