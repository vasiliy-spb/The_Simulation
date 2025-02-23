package bio.world.auxiliary_actions;

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
        List<Herbivore> herbivores = worldMap.getHerbivores();
        for (Herbivore herbivore : herbivores) {
            herbivore.makeMove(worldMap, pathFinder);
        }
    }
}
