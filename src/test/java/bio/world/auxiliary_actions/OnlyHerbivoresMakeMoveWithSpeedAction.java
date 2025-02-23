package bio.world.auxiliary_actions;

import bio.world.simulation.TickCounter;
import bio.world.map.WorldMap;
import bio.world.actions.Action;
import bio.world.entities.Herbivore;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;

import java.util.List;

public class OnlyHerbivoresMakeMoveWithSpeedAction implements Action {
    private final WorldMap worldMap;
    private final TickCounter tickCounter;
    private final PathFinder pathFinder;

    public OnlyHerbivoresMakeMoveWithSpeedAction(WorldMap worldMap, TickCounter tickCounter) {
        this.worldMap = worldMap;
        this.tickCounter = tickCounter;
        this.pathFinder = new AStarPathFinder(worldMap);
    }

    @Override
    public void perform() {
        int currentTick = tickCounter.getCurrentTick();
        List<Herbivore> herbivores = worldMap.getHerbivores();
        for (Herbivore herbivore : herbivores) {
            if (herbivore.shouldMove(currentTick)) {
                herbivore.makeMove(worldMap, pathFinder);
            }
        }
    }
}
