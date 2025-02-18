package bio.world.actions;

import bio.world.WorldMap;
import bio.world.entities.Creature;
import bio.world.path_finders.AStarPathFinder;
import bio.world.path_finders.PathFinder;
import bio.world.path_finders.RandomStepPathFinder;

import java.util.Set;

public class MakeMoveAction implements Action {
    private final WorldMap worldMap;
    private final PathFinder pathFinder;
    public MakeMoveAction(WorldMap worldMap) {
        this.worldMap = worldMap;
//        this.pathFinder = new RandomStepPathFinder(this.worldMap);
        this.pathFinder = new AStarPathFinder(this.worldMap);
    }

    @Override
    public void perform() {
        Set<Creature> creatureSet = worldMap.getCreatures();
        for (Creature creature : creatureSet) {
            creature.makeMove(worldMap, pathFinder);
        }
    }
}
