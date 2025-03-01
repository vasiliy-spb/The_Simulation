package bio.world.actions;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.entities.regular.Creature;
import bio.world.entities.temporary.Flash;
import bio.world.map.WorldMap;
import bio.world.simulation.TickCounter;

import java.util.List;

public class CreateFlashAction implements Action {

    private final WorldMap worldMap;
    private final TickCounter tickCounter;

    public CreateFlashAction(WorldMap worldMap, TickCounter tickCounter) {
        this.worldMap = worldMap;
        this.tickCounter = tickCounter;
    }

    @Override
    public void perform() {
        int currentTick = tickCounter.getCurrentTick();
        List<Entity> entities = worldMap.getAllEntities();

        for (Entity entity : entities) {
            if (entity instanceof Creature creature) {
                if (creature.wasShot()) {
                    Coordinates coordinates = creature.getCoordinates();
                    Flash flash = new Flash(coordinates, currentTick);
                    worldMap.addEntity(flash);
                }
            }
        }
    }
}
