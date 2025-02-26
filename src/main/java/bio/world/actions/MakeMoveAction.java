package bio.world.actions;

import bio.world.entities.Entity;
import bio.world.simulation.TickCounter;
import bio.world.map.WorldMap;
import bio.world.entities.Creature;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;

import java.util.List;

public class MakeMoveAction implements Action {
    private final WorldMap worldMap;
    private final PathFinder pathFinder;
    private final TickCounter tickCounter;

    public MakeMoveAction(WorldMap worldMap, TickCounter tickCounter) {
        this.worldMap = worldMap;
        this.pathFinder = new AStarPathFinder(this.worldMap);
        this.tickCounter = tickCounter;
    }

    @Override
    public void perform() {
        int currentTick = tickCounter.getCurrentTick();
        List<Entity> entities = worldMap.getAllEntities();

        for (Entity entity : entities) {
            if (entity instanceof Creature creature) {
                if (creature.shouldMove(currentTick)) {
                    creature.makeMove(worldMap, pathFinder);
                }
            }
        }
    }
}
