package bio.world.actions;

import bio.world.TickCounter;
import bio.world.WorldMap;
import bio.world.entities.Creature;
import bio.world.entities.Predator;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;

import java.util.Set;

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
        Set<Creature> creatureSet = worldMap.getCreatures();
        for (Creature creature : creatureSet) {
            if (creature instanceof Predator predator) {
                if (predator.shouldMove(currentTick)) {
                    predator.makeMove(worldMap, pathFinder);
                }
            }
        }
    }
}
