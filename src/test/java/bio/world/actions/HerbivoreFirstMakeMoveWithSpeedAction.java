package bio.world.actions;

import bio.world.TickCounter;
import bio.world.WorldMap;
import bio.world.entities.Creature;
import bio.world.entities.Herbivore;
import bio.world.entities.Predator;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;

import java.util.Set;

public class HerbivoreFirstMakeMoveWithSpeedAction implements Action {
    private final WorldMap worldMap;
    private final TickCounter tickCounter;
    private final PathFinder pathFinder;

    public HerbivoreFirstMakeMoveWithSpeedAction(WorldMap worldMap, TickCounter tickCounter) {
        this.worldMap = worldMap;
        this.tickCounter = tickCounter;
        this.pathFinder = new AStarPathFinder(worldMap);
    }

    @Override
    public void perform() {
        int currentTick = tickCounter.getCurrentTick();
        Set<Creature> creatureSet = worldMap.getCreatures();
        for (Creature creature : creatureSet) {
            if (creature instanceof Herbivore herbivore) {
                if (herbivore.shouldMove(currentTick)) {
                    herbivore.makeMove(worldMap, pathFinder);
                }
            }
        }
        for (Creature creature : creatureSet) {
            if (creature instanceof Predator predator) {
                if (predator.shouldMove(currentTick)) {
                    predator.makeMove(worldMap, pathFinder);
                }
            }
        }
    }
}
