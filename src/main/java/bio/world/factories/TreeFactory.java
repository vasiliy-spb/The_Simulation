package bio.world.factories;

import bio.world.Coordinates;
import bio.world.entities.Tree;

public class TreeFactory extends EntityFactory<Tree> {
    @Override
    public Tree createInstanceBy(Coordinates coordinates) {
        Tree tree = new Tree(coordinates);
        return tree;
    }
}
