package bio.world.actions;

import bio.world.entities.Entity;
import bio.world.map.WorldMap;
import bio.world.entities.Creature;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;

import java.util.List;

public class MakeMoveAction implements Action {
    private final WorldMap worldMap;
    private final PathFinder pathFinder;

    public MakeMoveAction(WorldMap worldMap) {
        this.worldMap = worldMap;
        this.pathFinder = new AStarPathFinder(this.worldMap);
    }

    @Override
    public void perform() {
        List<Entity> entities = worldMap.getAllEntities();
        for (Entity entity : entities) {
            if (entity instanceof Creature creature) {
                creature.makeMove(worldMap, pathFinder);
            }
        }
    }
}
