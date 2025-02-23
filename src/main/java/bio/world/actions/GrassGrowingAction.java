package bio.world.actions;

import bio.world.WorldMap;
import bio.world.entities.Grass;
import bio.world.entities.Herbivore;
import bio.world.factories.GrassFactory;

import java.util.List;

public class GrassGrowingAction implements Action {
    private final WorldMap worldMap;
    private final GrassFactory grassFactory;

    public GrassGrowingAction(WorldMap worldMap) {
        this.worldMap = worldMap;
        this.grassFactory = new GrassFactory();
    }

    @Override
    public void perform() {
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();

        int totalNumberOfFields = height * width;
        int numberOfOccupiedFields = worldMap.getBusyCoordinates().size();
        int numberOfEmptyFields = totalNumberOfFields - numberOfOccupiedFields;

        List<Grass> grasses = worldMap.getGrasses();
        List<Herbivore> herbivores = worldMap.getHerbivores();
        int herbivoreGrassDiff = herbivores.size() - grasses.size();

        if (!herbivores.isEmpty() && herbivoreGrassDiff <= 0) {
            return;
        }
        int countAdditionalGrass = 1;
        if (!herbivores.isEmpty()) {
            countAdditionalGrass = Math.min(numberOfEmptyFields / 2, herbivoreGrassDiff);
        }
        createGrass(countAdditionalGrass);
    }

    private void createGrass(int count) {
        while (count-- > 0) {
            Grass grass = grassFactory.createInstance(worldMap);
            worldMap.addEntity(grass);
        }
    }
}
