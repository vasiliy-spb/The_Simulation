package bio.world.actions;

import bio.world.TickCounter;
import bio.world.WorldMap;
import bio.world.entities.Creature;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;

import java.util.Set;

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
        Set<Creature> creatureSet = worldMap.getCreatures();
        int currentTick = tickCounter.getCurrentTick();
        for (Creature creature : creatureSet) {
            if (creature.shouldMove(currentTick)) {
//                System.out.println("—— Move " + creature.getClass().getSimpleName());
                creature.makeMove(worldMap, pathFinder);
            }
        }
    }
}
