package bio.world.factories;

import bio.world.entities.Coordinates;
import bio.world.entities.statical.Tree;

public class TreeFactory extends EntityFactory<Tree> {
    @Override
    public Tree createInstanceBy(Coordinates coordinates) {
        Tree tree = new Tree(coordinates);
        return tree;
    }
}
