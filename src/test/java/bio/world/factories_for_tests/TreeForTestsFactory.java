package bio.world.factories_for_tests;

import bio.world.entities.Coordinates;
import bio.world.map.WorldMap;
import bio.world.entities.Tree;

public class TreeForTestsFactory extends EntityFactoryForTests<Tree> {
    @Override
    public Tree createInstance(WorldMap worldMap) {
        Coordinates coordinates = createFreeCoordinates(worldMap);
        Tree tree = new Tree(coordinates);
        return tree;
    }

    @Override
    public Tree createInstanceByCoordinate(WorldMap worldMap, Coordinates coordinates) {
        Tree tree = new Tree(coordinates);
        return tree;
    }
}
