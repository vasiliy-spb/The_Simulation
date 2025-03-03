package bio.world.actions;

import bio.world.entities.Entity;
import bio.world.entities.regular.Creature;
import bio.world.entities.regular.Huntsman;
import bio.world.entities.statical.trap.Trap;
import bio.world.map.WorldMap;

import java.util.List;

public class RemoveTrapAction implements Action {
    private final WorldMap worldMap;

    public RemoveTrapAction(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public void perform() {
        List<Entity> entities = worldMap.getAllEntities();

        for (Entity entity : entities) {
            if (entity instanceof Trap trap) {
                if (trap.hasCapturedCreature()) {
                    Creature creature = trap.getCapturedCreature().get();
                    if (!creature.isAlive()) {
                        worldMap.removeEntity(trap);
                        Huntsman trapOwner = trap.getOwner();
                        trapOwner.increaseAvailableTrapCount();
                    }
                }
            }
        }
    }
}
