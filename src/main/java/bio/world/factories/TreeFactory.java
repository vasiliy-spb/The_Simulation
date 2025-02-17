package bio.world.factories;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.entities.Rock;
import bio.world.entities.Tree;

public class TreeFactory extends EntityFactory<Tree> {
    @Override
    public Tree createInstance(WorldMap worldMap) {
        Coordinates coordinates;
        do {
            int height = worldMap.getHeight();
            int width = worldMap.getWidth();
            int row = random.nextInt(height + 1);
            int column = random.nextInt(width + 1);
            coordinates = new Coordinates(row, column);
        } while (worldMap.areBusy(coordinates));
        Tree tree = new Tree(coordinates);
        return tree;
    }
}
