package bio.world.factories;

import bio.world.entities.Coordinates;
import bio.world.entities.statical.Rock;

public class RockFactory extends EntityFactory<Rock> {
    @Override
    public Rock createInstanceBy(Coordinates coordinates) {
        Rock rock = new Rock(coordinates);
        return rock;
    }
}
