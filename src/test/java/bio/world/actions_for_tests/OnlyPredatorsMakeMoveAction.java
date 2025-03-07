package bio.world.actions_for_tests;

import bio.world.entities.Entity;
import bio.world.map.WorldMap;
import bio.world.actions.Action;
import bio.world.entities.Predator;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;

import java.util.List;

public class OnlyPredatorsMakeMoveAction implements Action {
    private final WorldMap worldMap;
    private final PathFinder pathFinder;

    public OnlyPredatorsMakeMoveAction(WorldMap worldMap) {
        this.worldMap = worldMap;
        this.pathFinder = new AStarPathFinder(this.worldMap);
    }

    @Override
    public void perform() {
        List<Entity> entities = worldMap.getAllEntities();
        for (Entity entity : entities) {
            if (entity instanceof Predator predator) {
                predator.makeMove(worldMap, pathFinder);
            }
        }
    }
}
