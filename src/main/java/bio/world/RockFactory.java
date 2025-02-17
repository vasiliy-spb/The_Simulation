package bio.world;

import bio.world.entities.Rock;

public class RockFactory extends EntityFactory<Rock> {
    @Override
    public Rock createInstance(WorldMap worldMap) {
        Coordinates coordinates;
        do {
            int height = worldMap.getHeight();
            int width = worldMap.getWidth();
            int row = random.nextInt(height + 1);
            int column = random.nextInt(width + 1);
            coordinates = new Coordinates(row, column);
        } while (worldMap.areBusy(coordinates));
        Rock rock = new Rock(coordinates);
        return rock;
    }
}
