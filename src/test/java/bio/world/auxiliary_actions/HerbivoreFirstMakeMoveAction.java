package bio.world.auxiliary_actions;

import bio.world.WorldMap;
import bio.world.actions.Action;
import bio.world.entities.Creature;
import bio.world.entities.Herbivore;
import bio.world.entities.Predator;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;

import java.util.List;

public class HerbivoreFirstMakeMoveAction implements Action {
    private final WorldMap worldMap;
    private final PathFinder pathFinder;

    public HerbivoreFirstMakeMoveAction(WorldMap worldMap) {
        this.worldMap = worldMap;
        this.pathFinder = new AStarPathFinder(this.worldMap);
    }

    @Override
    public void perform() {
        List<Creature> creatures = worldMap.getCreatures();
        for (Creature creature : creatures) {
            if (creature instanceof Herbivore herbivore) {
                herbivore.makeMove(worldMap, pathFinder);
            }
        }
        for (Creature creature : creatures) {
            if (creature instanceof Predator predator) {
                predator.makeMove(worldMap, pathFinder);
            }
        }
    }
}
