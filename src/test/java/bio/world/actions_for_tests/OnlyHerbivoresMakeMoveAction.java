package bio.world.actions_for_tests;

import bio.world.entities.Entity;
import bio.world.map.WorldMap;
import bio.world.actions.Action;
import bio.world.entities.Herbivore;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;

import java.util.List;

public class OnlyHerbivoresMakeMoveAction implements Action {
    private final WorldMap worldMap;
    private final PathFinder pathFinder;

    public OnlyHerbivoresMakeMoveAction(WorldMap worldMap) {
        this.worldMap = worldMap;
        this.pathFinder = new AStarPathFinder(this.worldMap);
    }

    @Override
    public void perform() {
        List<Entity> entities = worldMap.getAllEntities();
        for (Entity entity : entities) {
            if (entity instanceof Herbivore herbivore) {
                herbivore.makeMove(worldMap, pathFinder);
            }
        }
    }
}
