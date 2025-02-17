package bio.world.factories;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.entities.Tree;

public class TreeFactory extends EntityFactory<Tree> {
    @Override
    public Tree createInstance(WorldMap worldMap) {
        Coordinates coordinates = createFreeCoordinates(worldMap);
        Tree tree = new Tree(coordinates);
        return tree;
    }
}
