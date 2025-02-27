package bio.world.actions;

import bio.world.entities.Entity;
import bio.world.entities.temporary.TemporaryEntity;
import bio.world.map.WorldMap;
import bio.world.simulation.TickCounter;

import java.util.List;

public class RemoveTemporaryEntityAction implements Action {
    private final WorldMap worldMap;
    private final TickCounter tickCounter;

    public RemoveTemporaryEntityAction(WorldMap worldMap, TickCounter tickCounter) {
        this.worldMap = worldMap;
        this.tickCounter = tickCounter;
    }

    @Override
    public void perform() {
        int currentTick = tickCounter.getCurrentTick();
        List<Entity> entities = worldMap.getAllEntities();

        for (Entity entity : entities) {
            if (entity instanceof TemporaryEntity temporaryEntity) {
                if (temporaryEntity.isLifeTimeOver(currentTick)) {
                    worldMap.removeEntity(temporaryEntity);
                }
            }
        }
    }
}
