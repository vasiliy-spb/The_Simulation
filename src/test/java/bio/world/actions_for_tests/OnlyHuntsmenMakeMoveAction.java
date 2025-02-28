package bio.world.actions_for_tests;

import bio.world.actions.Action;
import bio.world.entities.Entity;
import bio.world.entities.regular.Herbivore;
import bio.world.entities.regular.Huntsmen;
import bio.world.map.WorldMap;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;
import bio.world.simulation.TickCounter;

import java.util.List;

public class OnlyHuntsmenMakeMoveAction implements Action {
    private final WorldMap worldMap;
    private final TickCounter tickCounter;
    private final PathFinder pathFinder;

    public OnlyHuntsmenMakeMoveAction(WorldMap worldMap, TickCounter tickCounter) {
        this.worldMap = worldMap;
        this.tickCounter = tickCounter;
        this.pathFinder = new AStarPathFinder(worldMap);
    }

    @Override
    public void perform() {
        int currentTick = tickCounter.getCurrentTick();
        System.out.println("currentTick = " + currentTick);

        List<Entity> entities = worldMap.getAllEntities();
        for (Entity entity : entities) {
            if (entity instanceof Huntsmen huntsmen) {
                if (huntsmen.shouldMove(currentTick)) {
                    huntsmen.makeMove(worldMap, pathFinder);
                }
            }
        }
    }
}
