package bio.world.auxiliary_actions;

import bio.world.simulation.TickCounter;
import bio.world.map.WorldMap;
import bio.world.actions.Action;
import bio.world.entities.Creature;
import bio.world.entities.Herbivore;
import bio.world.entities.Predator;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;

import java.util.List;

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
        List<Creature> creatures = worldMap.getCreatures();
        for (Creature creature : creatures) {
            if (creature instanceof Herbivore herbivore) {
                if (herbivore.shouldMove(currentTick)) {
                    herbivore.makeMove(worldMap, pathFinder);
                }
            }
        }
        for (Creature creature : creatures) {
            if (creature instanceof Predator predator) {
                if (predator.shouldMove(currentTick)) {
                    predator.makeMove(worldMap, pathFinder);
                }
            }
        }
    }
}
