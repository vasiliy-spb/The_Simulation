package bio.world.actions;

import bio.world.TickCounter;
import bio.world.WorldMap;
import bio.world.entities.Creature;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;

import java.util.List;

public class MakeMoveWithSpeedAction implements Action {
    private final WorldMap worldMap;
    private final PathFinder pathFinder;
    private final TickCounter tickCounter;

    public MakeMoveWithSpeedAction(WorldMap worldMap, TickCounter tickCounter) {
        this.worldMap = worldMap;
        this.pathFinder = new AStarPathFinder(this.worldMap);
        this.tickCounter = tickCounter;
    }

    @Override
    public void perform() {
        List<Creature> creatures = worldMap.getCreatures();
        int currentTick = tickCounter.getCurrentTick();
        for (Creature creature : creatures) {
            if (creature.shouldMove(currentTick)) {
                creature.makeMove(worldMap, pathFinder);
            }
        }
    }
}
