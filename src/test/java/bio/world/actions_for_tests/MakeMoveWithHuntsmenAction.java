package bio.world.actions_for_tests;

import bio.world.actions.Action;
import bio.world.entities.Creature;
import bio.world.entities.Entity;
import bio.world.entities.Huntsmen;
import bio.world.map.WorldMap;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;
import bio.world.simulation.TickCounter;

import java.util.List;

public class MakeMoveWithHuntsmenAction implements Action {
    private final WorldMap worldMap;
    private final PathFinder pathFinder;
    private final TickCounter tickCounter;

    public MakeMoveWithHuntsmenAction(WorldMap worldMap, TickCounter tickCounter) {
        this.worldMap = worldMap;
        this.pathFinder = new AStarPathFinder(this.worldMap);
        this.tickCounter = tickCounter;
    }

    @Override
    public void perform() {
        int currentTick = tickCounter.getCurrentTick();
        System.out.println("currentTick = " + currentTick);
        List<Entity> entities = worldMap.getAllEntities();

        for (Entity entity : entities) {
            if (entity instanceof Creature creature) {
                if (creature.shouldMove(currentTick)) {
                    creature.makeMove(worldMap, pathFinder);
                }
            }
        }
        for (Entity entity : entities) {
            if (entity instanceof Huntsmen huntsmen) {
                if (huntsmen.shouldMove(currentTick)) {
                    huntsmen.makeMove(worldMap, pathFinder);
                }
            }
        }
    }
}
