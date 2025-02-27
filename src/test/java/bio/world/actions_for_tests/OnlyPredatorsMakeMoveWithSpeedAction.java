package bio.world.actions_for_tests;

import bio.world.entities.Entity;
import bio.world.simulation.TickCounter;
import bio.world.map.WorldMap;
import bio.world.actions.Action;
import bio.world.entities.Predator;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;

import java.util.List;

public class OnlyPredatorsMakeMoveWithSpeedAction implements Action {
    private final WorldMap worldMap;
    private final TickCounter tickCounter;
    private final PathFinder pathFinder;

    public OnlyPredatorsMakeMoveWithSpeedAction(WorldMap worldMap, TickCounter tickCounter) {
        this.worldMap = worldMap;
        this.tickCounter = tickCounter;
        this.pathFinder = new AStarPathFinder(worldMap);
    }

    @Override
    public void perform() {
        int currentTick = tickCounter.getCurrentTick();
        List<Entity> entities = worldMap.getAllEntities();
        for (Entity entity : entities) {
            if (entity instanceof Predator predator) {
                if (predator.shouldMove(currentTick)) {
                    predator.makeMove(worldMap, pathFinder);
                }
            }
        }
    }
}
