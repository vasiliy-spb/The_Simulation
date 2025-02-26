package bio.world.actions;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.simulation.TickCounter;
import bio.world.map.WorldMap;
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
        if (shouldSkipMove()) {
            return;
        }

        List<Coordinates> emptyCoordinates = createEmptyCoordinates();
        if (hasNotEnoughEmptyCells(emptyCoordinates)) {
            return;
        }

        Collections.shuffle(emptyCoordinates);

        int countAdditionalGrass = calculateCountAdditionalGrass(emptyCoordinates);
        growGrass(countAdditionalGrass, emptyCoordinates);
    }

    private boolean shouldSkipMove() {
        return tickCounter.getCurrentTick() % 2 == 0;
    }

    private List<Coordinates> createEmptyCoordinates() {
        Set<Coordinates> busyCoordinates = worldMap.getBusyCoordinates();
        List<Coordinates> emptyCoordinates = new ArrayList<>();
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Coordinates coordinates = new Coordinates(i, j);

                if (busyCoordinates.contains(coordinates)) {
                    continue;
                }

                emptyCoordinates.add(coordinates);
                busyCoordinates.add(coordinates);
            }
        }

        return emptyCoordinates;
    }

    private static boolean hasNotEnoughEmptyCells(List<Coordinates> emptyCoordinates) {
        return emptyCoordinates.size() < 2;
    }

    private int calculateCountAdditionalGrass(List<Coordinates> emptyCoordinates) {
        int herbivoreGrassBalance = calculateHerbivoreGrassBalance();
        int countAdditionalGrass = 1;

        if (herbivoreGrassBalance > 0) {
            countAdditionalGrass = Math.min(emptyCoordinates.size() / 2, herbivoreGrassBalance / 2);
            countAdditionalGrass = Math.max(countAdditionalGrass, 1);
        }

        return countAdditionalGrass;
    }

    private int calculateHerbivoreGrassBalance() {
        List<Entity> entities = worldMap.getAllEntities();

        List<Entity> grassEntities = entities.stream()
                .filter(e -> e instanceof Grass)
                .toList();

        List<Entity> herbivoreEntities = entities.stream()
                .filter(e -> e instanceof Herbivore)
                .toList();

        return herbivoreEntities.size() - grassEntities.size();
    }

    private void growGrass(int countAdditionalGrass, List<Coordinates> emptyCoordinates) {
        for (int i = 0; i < countAdditionalGrass; i++) {
            Coordinates coordinates = emptyCoordinates.get(i);
            Grass grass = grassFactory.createInstanceBy(coordinates);
            worldMap.addEntity(grass);
        }
    }
}
