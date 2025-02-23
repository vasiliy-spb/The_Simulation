package bio.world.actions;

import bio.world.Coordinates;
import bio.world.TickCounter;
import bio.world.WorldMap;
import bio.world.entities.Grass;
import bio.world.entities.Herbivore;
import bio.world.factories.GrassFactory;

import java.util.*;

public class GrassGrowingAction implements Action {
    private final WorldMap worldMap;
    private final TickCounter tickCounter;
    private final GrassFactory grassFactory;

    public GrassGrowingAction(WorldMap worldMap, TickCounter tickCounter) {
        this.worldMap = worldMap;
        this.tickCounter = tickCounter;
        this.grassFactory = new GrassFactory();
    }

    @Override
    public void perform() {
        if (tickCounter.getCurrentTick() % 2 == 0) {
            return;
        }

        List<Coordinates> emptyCoordinates = createEmptyCoordinates();
        if (emptyCoordinates.size() < 2) {
            return;
        }

        Collections.shuffle(emptyCoordinates);

        int countAdditionalGrass = calculateCountAdditionalGrass(emptyCoordinates);
        growGrass(countAdditionalGrass, emptyCoordinates);
    }

    private void growGrass(int countAdditionalGrass, List<Coordinates> emptyCoordinates) {
        for (int i = 0; i < countAdditionalGrass; i++) {
            Coordinates coordinates = emptyCoordinates.get(i);
            Grass grass = grassFactory.createInstanceBy(coordinates);
            worldMap.addEntity(grass);
        }
    }

    private int calculateCountAdditionalGrass(List<Coordinates> emptyCoordinates) {
        List<Grass> grasses = worldMap.getGrasses();
        List<Herbivore> herbivores = worldMap.getHerbivores();
        int herbivoreGrassDiff = herbivores.size() - grasses.size();

        int countAdditionalGrass = 1;
        if (!herbivores.isEmpty()) {
            countAdditionalGrass = Math.min(emptyCoordinates.size() / 2, herbivoreGrassDiff / 2);
            countAdditionalGrass = Math.max(countAdditionalGrass, 1);
        }
        return countAdditionalGrass;
    }

    private List<Coordinates> createEmptyCoordinates() {
        Set<Coordinates> occupiedCoordinates = worldMap.getBusyCoordinates();
        List<Coordinates> emptyCoordinates = new ArrayList<>();

        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Coordinates coordinates = new Coordinates(i, j);
                if (occupiedCoordinates.contains(coordinates)) {
                    continue;
                }
                emptyCoordinates.add(coordinates);
            }
        }
        return emptyCoordinates;
    }
}
